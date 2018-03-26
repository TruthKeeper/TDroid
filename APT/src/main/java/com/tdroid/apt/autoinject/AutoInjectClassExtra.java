package com.tdroid.apt.autoinject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tdroid.apt.AbsClassCell;
import com.tdroid.apt.Utils;
import com.tdroid.annotation.AutoInject;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/16
 *     desc   : xxxx描述
 * </pre>
 */
public class AutoInjectClassExtra extends AbsClassCell {
    private static final ClassName HELPER = ClassName.get("com.tk.tdroid.autoinject", "IAutoInject");
    private static final String SUFFIX = "_AutoInject";
    private static TypeMirror typeActivity;
    private static TypeMirror typeFragment;
    private static TypeMirror typeFragmentV4;

    public AutoInjectClassExtra(TypeElement clsElement, Elements elementUtils, Filer fileCreator, Messager messager, Types typeUtils) {
        super(clsElement, elementUtils, fileCreator, messager, typeUtils);
        typeActivity = elementUtils.getTypeElement("android.app.Activity").asType();
        typeFragment = elementUtils.getTypeElement("android.app.Fragment").asType();
        typeFragmentV4 = elementUtils.getTypeElement("android.support.v4.app.Fragment").asType();
    }

    /**
     * 生成代码
     */
    @Override
    public void generateCode() {
        try {
            final TypeName clsName = ClassName.get(clsElement.asType());
            final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addJavadoc("from APT auto")
                    .returns(void.class)
                    .addParameter(clsName, "object");

            final CodeBlock.Builder codeBuilder = CodeBlock.builder();
            if (typeUtils.isSubtype(clsElement.asType(), typeActivity)) {
                codeBuilder.addStatement("android.os.Bundle bundle = object.getIntent().getExtras()");
            } else if (typeUtils.isSubtype(clsElement.asType(), typeFragment)) {
                codeBuilder.addStatement("android.os.Bundle bundle = object.getArguments()");
            } else if (typeUtils.isSubtype(clsElement.asType(), typeFragmentV4)) {
                codeBuilder.addStatement("android.os.Bundle bundle = object.getArguments()");
            } else {
                return;
            }
            codeBuilder.addStatement("if (bundle == null) return");
            for (VariableElement element : elementList) {
                Name fieldName = element.getSimpleName();
                String fieldType = element.asType().toString();
                String bundleFieldType = Utils.getBundleFieldType(fieldType);
                AutoInject annotation = element.getAnnotation(AutoInject.class);
                if (bundleFieldType != null) {
                    codeBuilder.addStatement(String.format("object.$N = bundle.get%s($S)", bundleFieldType),
                            fieldName, Utils.isEmpty(annotation.name()) ? fieldName : annotation.name());
                    if (annotation.strict()) {
                        codeBuilder.addStatement(String.format("if (object.$N == null) throw new java.lang.NullPointerException(\"%s%s\")",
                                fieldName, " is null in strict"), fieldName);
                    }
                } else if (annotation.parcelable()) {
                    //用Parcelable接口转换
                    codeBuilder.addStatement("object.$N = bundle.getParcelable($S)",
                            fieldName, Utils.isEmpty(annotation.name()) ? fieldName : annotation.name());
                    if (annotation.strict()) {
                        codeBuilder.addStatement(String.format("if (object.$N == null) throw new java.lang.NullPointerException(\"%s%s\")",
                                fieldName, " is null in strict"), fieldName);
                    }
                }
            }
            methodBuilder.addCode(codeBuilder.build());

            String className = clsElement.getSimpleName().toString() + SUFFIX;
            TypeSpec typeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(HELPER, clsName))
                    .addJavadoc("from APT auto")
                    .addMethod(methodBuilder.build())
                    .build();

            JavaFile javaFile = JavaFile.builder(elementUtils.getPackageOf(clsElement).getQualifiedName().toString(),
                    typeSpec).build();
            // 在module/build/generated/source/apt 生成
            javaFile.writeTo(fileCreator);
        } catch (Exception e) {
        }
    }
}
