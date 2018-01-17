package com.tdroid.save;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tdroid.Utils;
import com.tdroid.annotation.Save;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/16
 *     desc   : xxxx描述
 * </pre>
 */
class SaveClassExtra {
    private static final ClassName HELPER = ClassName.get("com.tk.tdroid.save", "ISaveHelper");
    private static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
    private static final String SUFFIX = "_Save";
    private List<VariableElement> elementList = new LinkedList<>();

    private TypeElement clsElement;

    private Elements elementUtils;
    private Filer fileCreator;
    private Messager messager;

    SaveClassExtra(TypeElement clsElement, Elements elementUtils, Filer fileCreator, Messager messager) {
        this.clsElement = clsElement;
        this.elementUtils = elementUtils;
        this.fileCreator = fileCreator;
        this.messager = messager;
    }

    void addField(VariableElement element) {
        elementList.add(element);
    }

    /**
     * 生成代码
     */
    void generateCode() {
        try {
            TypeName clsName = ClassName.get(clsElement.asType());
            MethodSpec.Builder saveMethodBuilder;
            MethodSpec.Builder restoreMethodBuilder;
            saveMethodBuilder = MethodSpec.methodBuilder("save")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addJavadoc("from APT auto")
                    .returns(void.class)
                    .addParameter(clsName, "object")
                    .addParameter(BUNDLE, "outState");

            restoreMethodBuilder = MethodSpec.methodBuilder("restore")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addJavadoc("from APT auto")
                    .returns(void.class)
                    .addParameter(clsName, "object")
                    .addParameter(BUNDLE, "savedInstanceState");

            CodeBlock.Builder saveMethodCodeBuilder = CodeBlock.builder();
            CodeBlock.Builder restoreMethodCodeBuilder = CodeBlock.builder();
            restoreMethodCodeBuilder.beginControlFlow("if(savedInstanceState != null)");

            for (VariableElement element : elementList) {
                Name fieldName = element.getSimpleName();
                String fieldType = element.asType().toString();
                String bundleFieldType = Utils.getBundleFieldType(fieldType);
                messager.printMessage(Diagnostic.Kind.NOTE, fieldType);
                if (bundleFieldType == null) {
                    Save annotation = element.getAnnotation(Save.class);
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

            MethodSpec saveMethod = saveMethodBuilder.build();
            MethodSpec recoverMethod = restoreMethodBuilder.build();
            String className = clsElement.getSimpleName().toString() + SUFFIX;
            TypeSpec typeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(HELPER, clsName))
                    .addJavadoc("from APT auto")
                    .addMethod(saveMethod)
                    .addMethod(recoverMethod)
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
