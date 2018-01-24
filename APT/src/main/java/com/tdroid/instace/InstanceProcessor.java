package com.tdroid.instace;

import com.tdroid.AnnotationProcessor;
import com.tdroid.IProcessor;
import com.tdroid.Utils;
import com.tdroid.annotation.Instance;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;


/**
 * <pre>
 *      author : TK
 *      time : 2017/9/21
 *      desc : 生成反射实例化的工厂类
 * </pre>
 */

public class InstanceProcessor implements IProcessor {
    public static final String CLASS_NAME = "InstanceFactory";
    public static final String METHOD_NAME = "create";
    public static final String DEFAULT_PATH = "com.apt";

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor annotationProcessor) {
        //.gradle配置的全路径
        String createPath = annotationProcessor.getOptions().get(CLASS_NAME);
        if (Utils.isEmpty(createPath)) {
            createPath = DEFAULT_PATH;
        } else {
            int index = createPath.lastIndexOf(".");
            if (index == -1) {
                annotationProcessor.getMessager().printMessage(Diagnostic.Kind.NOTE, CLASS_NAME + "：全类名配置错误");
                return;
            }
            createPath = createPath.substring(0, index);
        }
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(CLASS_NAME)
                //public final 的类
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("实例化工厂 , from APT auto");

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addJavadoc("实例化 , from APT auto")
                .addTypeVariable(TypeVariableName.get("T"))
                .addParameter(TypeVariableName.get("Class<T>", TypeName.get(List.class)), "cls")
                .returns(TypeVariableName.get("T"));

        List<String> classNameList = new ArrayList<>();
        CodeBlock.Builder blockBuilder = CodeBlock.builder();
        blockBuilder.beginControlFlow("if(cls==null)");
        blockBuilder.addStatement("return null");
        blockBuilder.endControlFlow();
        blockBuilder.beginControlFlow("try");
        blockBuilder.beginControlFlow("switch (cls.getName())");
        try {
            //被注解修饰的集合
            Set<TypeElement> typeElements = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(Instance.class));
            for (TypeElement typeElement : typeElements) {
                annotationProcessor.getMessager().printMessage(Diagnostic.Kind.NOTE, "解析：" + typeElement.toString());
                if (!Utils.isValidClass(annotationProcessor.getMessager(), typeElement)) {
                    //过滤非有效类
                    continue;
                }
                ClassName clsName = ClassName.get(typeElement);
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (String s : clsName.simpleNames()) {
                    if (!first) {
                        builder.append(".");
                    }
                    builder.append(s);
                    first = false;
                }
                String fullName = builder.toString();
                if (classNameList.contains(fullName)) {
                    continue;
                }
                classNameList.add(fullName);
                //被注解修饰的可以直接new生成
                blockBuilder.addStatement("case $S: return (T) new $T()", clsName.reflectionName(), clsName);
            }
            blockBuilder.addStatement("default : return cls.newInstance()");
            blockBuilder.endControlFlow();
            blockBuilder.endControlFlow();
            blockBuilder.beginControlFlow("catch(Exception e)");
            blockBuilder.addStatement("e.printStackTrace()");
            blockBuilder.endControlFlow();
            blockBuilder.addStatement("return null");

            methodBuilder.addCode(blockBuilder.build());
            classBuilder.addMethod(methodBuilder.build());
            // 生成Java源代码
            JavaFile javaFile = JavaFile.builder(createPath, classBuilder.build()).build();
            // 在module/build/generated/source/apt 生成
            javaFile.writeTo(annotationProcessor.getFileCreator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
