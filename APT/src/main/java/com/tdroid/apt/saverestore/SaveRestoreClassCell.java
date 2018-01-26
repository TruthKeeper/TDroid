package com.tdroid.apt.saverestore;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tdroid.apt.AbsClassCell;
import com.tdroid.apt.Utils;
import com.tdroid.annotation.SaveAndRestore;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/16
 *     desc   : xxxx描述
 * </pre>
 */
public class SaveRestoreClassCell extends AbsClassCell {
    private static final ClassName HELPER = ClassName.get("com.tk.tdroid.saverestore", "ISaveRestore");
    private static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
    private static final String SUFFIX = "_SaveRestore";

    public SaveRestoreClassCell(TypeElement clsElement, Elements elementUtils, Filer fileCreator, Messager messager, Types typeUtils) {
        super(clsElement, elementUtils, fileCreator, messager, typeUtils);
    }

    /**
     * 生成代码
     */
    @Override
    public void generateCode() {
        try {
            final TypeName clsName = ClassName.get(clsElement.asType());

            final MethodSpec.Builder saveMethodBuilder = MethodSpec.methodBuilder("save")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addJavadoc("from APT auto")
                    .returns(void.class)
                    .addParameter(clsName, "object")
                    .addParameter(BUNDLE, "outState");

            final MethodSpec.Builder restoreMethodBuilder = MethodSpec.methodBuilder("restore")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addJavadoc("from APT auto")
                    .returns(void.class)
                    .addParameter(clsName, "object")
                    .addParameter(BUNDLE, "savedInstanceState");

            final CodeBlock.Builder saveMethodCodeBuilder = CodeBlock.builder();
            final CodeBlock.Builder restoreMethodCodeBuilder = CodeBlock.builder();
            restoreMethodCodeBuilder.beginControlFlow("if(savedInstanceState != null)");

            for (VariableElement element : elementList) {
                Name fieldName = element.getSimpleName();
                String fieldType = element.asType().toString();
                String bundleFieldType = Utils.getBundleFieldType(fieldType);
                if (bundleFieldType == null) {
                    SaveAndRestore annotation = element.getAnnotation(SaveAndRestore.class);
                    if (annotation.gson()) {
                        //通过Gson转换
                        saveMethodCodeBuilder.addStatement("outState.putString($S,new com.google.gson.Gson().toJson(object.$N))",
                                fieldName, fieldName);
                        restoreMethodCodeBuilder.addStatement("object.$N = new com.google.gson.Gson().fromJson(savedInstanceState.getString($S),$N.class)",
                                fieldName, fieldName, fieldType);
                    } else if (annotation.parcelable()) {
                        //通过Parcelable转换
                        saveMethodCodeBuilder.addStatement("outState.putParcelable($S,object.$N)",
                                fieldName, fieldName);
                        restoreMethodCodeBuilder.addStatement("object.$N = savedInstanceState.getParcelable($S)",
                                fieldName, fieldName);
                    }
                } else {
                    saveMethodCodeBuilder.addStatement(String.format("outState.put%s($S,object.$N)", bundleFieldType),
                            fieldName, fieldName);
                    restoreMethodCodeBuilder.addStatement(String.format("object.$N = savedInstanceState.get%s($S)", bundleFieldType),
                            fieldName, fieldName);
                }
            }
            //关闭}
            restoreMethodCodeBuilder.endControlFlow();
            saveMethodBuilder.addCode(saveMethodCodeBuilder.build());
            restoreMethodBuilder.addCode(restoreMethodCodeBuilder.build());

            String className = clsElement.getSimpleName().toString() + SUFFIX;
            TypeSpec typeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(HELPER, clsName))
                    .addJavadoc("from APT auto")
                    .addMethod(saveMethodBuilder.build())
                    .addMethod(restoreMethodBuilder.build())
                    .build();

            JavaFile javaFile = JavaFile.builder(elementUtils.getPackageOf(clsElement).getQualifiedName().toString(),
                    typeSpec).build();
            //写入
            javaFile.writeTo(fileCreator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
