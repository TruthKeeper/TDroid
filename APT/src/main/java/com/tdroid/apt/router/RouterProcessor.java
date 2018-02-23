package com.tdroid.apt.router;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.tdroid.apt.AnnotationProcessor;
import com.tdroid.apt.IProcessor;
import com.tdroid.apt.Utils;
import com.tdroid.annotation.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;


/**
 * <pre>
 *      author : TK
 *      time : 2018/1/25
 *      desc : 生成反射实例化的工厂类
 * </pre>
 */

public class RouterProcessor implements IProcessor {


    public static final String CLASS_NAME = "RouterTable";
    public static final String METHOD_ACTIVITY = "getActivityMap";
    public static final String METHOD_SERVICE = "getServiceMap";
    public static final String METHOD_FRAGMENT = "getFragmentMap";
    public static final String METHOD_FRAGMENT_V4 = "getFragmentV4Map";
    public static final String DEFAULT_PATH = "com.apt";


    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor annotationProcessor) {
        //.gradle配置的全路径
        final String moduleName = annotationProcessor.getOptions().get(AnnotationProcessor.MODULE_NAME);
        StringBuilder createPath = new StringBuilder(DEFAULT_PATH);
        if (!Utils.isEmpty(moduleName)) {
            createPath.append(".");
            createPath.append(moduleName);
        }

        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ClassName.get("com.tk.tdroid.router", "IRouterTable"))
                .addJavadoc("路由表 , from APT auto");

        final MethodSpec.Builder activityBuilder = MethodSpec.methodBuilder(METHOD_ACTIVITY)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addJavadoc("获取Activity映射表 , from APT auto")
                .returns(TypeVariableName.get("java.util.Map<String, Class<?>>"));
        final MethodSpec.Builder serviceBuilder = MethodSpec.methodBuilder(METHOD_SERVICE)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addJavadoc("获取Service映射表 , from APT auto")
                .returns(TypeVariableName.get("java.util.Map<String, Class<?>>"));
        final MethodSpec.Builder fragmentBuilder = MethodSpec.methodBuilder(METHOD_FRAGMENT)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addJavadoc("获取Fragment映射表 , from APT auto")
                .returns(TypeVariableName.get("java.util.Map<String, Class<?>>"));
        final MethodSpec.Builder fragmentV4Builder = MethodSpec.methodBuilder(METHOD_FRAGMENT_V4)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addJavadoc("获取FragmentV4映射表 , from APT auto")
                .returns(TypeVariableName.get("java.util.Map<String, Class<?>>"));

        final CodeBlock.Builder activityCodeBuilder = CodeBlock.builder();
        activityCodeBuilder.addStatement("java.util.Map<String, Class<?>> map = new java.util.HashMap<>()");
        final CodeBlock.Builder serviceCodeBuilder = CodeBlock.builder();
        serviceCodeBuilder.addStatement("java.util.Map<String, Class<?>> map = new java.util.HashMap<>()");
        final CodeBlock.Builder fragmentCodeBuilder = CodeBlock.builder();
        fragmentCodeBuilder.addStatement("java.util.Map<String, Class<?>> map = new java.util.HashMap<>()");
        final CodeBlock.Builder fragmentV4CodeBuilder = CodeBlock.builder();
        fragmentV4CodeBuilder.addStatement("java.util.Map<String, Class<?>> map = new java.util.HashMap<>()");

        final TypeMirror typeActivity = annotationProcessor.getElementUtils()
                .getTypeElement("android.app.Activity").asType();
        final TypeMirror typeService = annotationProcessor.getElementUtils()
                .getTypeElement("android.app.Service").asType();
        final TypeMirror typeFragment = annotationProcessor.getElementUtils()
                .getTypeElement("android.app.Fragment").asType();
        final TypeMirror typeFragmentV4 = annotationProcessor.getElementUtils()
                .getTypeElement("android.support.v4.app.Fragment").asType();
        try {
            final List<String> classNameList = new ArrayList<>();
            //被注解修饰的集合
            Set<TypeElement> typeElements = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(Router.class));
            for (TypeElement typeElement : typeElements) {
                annotationProcessor.getMessager().printMessage(Diagnostic.Kind.NOTE, "解析：" + typeElement.toString());
                if (!Utils.isValidClass(annotationProcessor.getMessager(), typeElement)) {
                    //过滤非有效类
                    continue;
                }
                ClassName clsName = ClassName.get(typeElement);
                String fullName = Utils.getFullName(clsName);
                if (classNameList.contains(fullName)) {
                    continue;
                }
                classNameList.add(fullName);
                //路由路径
                String[] routerPathArray = typeElement.getAnnotation(Router.class).path();
                for (String routerPath : routerPathArray) {
                    if (annotationProcessor.getTypeUtils().isSubtype(typeElement.asType(), typeActivity)) {
                        activityCodeBuilder.addStatement("map.put($S , $T.class)", routerPath, clsName);
                    } else if (annotationProcessor.getTypeUtils().isSubtype(typeElement.asType(), typeService)) {
                        serviceCodeBuilder.addStatement("map.put($S , $T.class)", routerPath, clsName);
                    } else if (annotationProcessor.getTypeUtils().isSubtype(typeElement.asType(), typeFragment)) {
                        fragmentCodeBuilder.addStatement("map.put($S , $T.class)", routerPath, clsName);
                    } else if (annotationProcessor.getTypeUtils().isSubtype(typeElement.asType(), typeFragmentV4)) {
                        fragmentV4CodeBuilder.addStatement("map.put($S , $T.class)", routerPath, clsName);
                    }
                }
            }

            activityCodeBuilder.addStatement("return map");
            serviceCodeBuilder.addStatement("return map");
            fragmentCodeBuilder.addStatement("return map");
            fragmentV4CodeBuilder.addStatement("return map");

            activityBuilder.addCode(activityCodeBuilder.build());
            serviceBuilder.addCode(serviceCodeBuilder.build());
            fragmentBuilder.addCode(fragmentCodeBuilder.build());
            fragmentV4Builder.addCode(fragmentV4CodeBuilder.build());

            classBuilder.addMethod(activityBuilder.build());
            classBuilder.addMethod(serviceBuilder.build());
            classBuilder.addMethod(fragmentBuilder.build());
            classBuilder.addMethod(fragmentV4Builder.build());
            // 生成Java源代码
            JavaFile javaFile = JavaFile.builder(createPath.toString(), classBuilder.build()).build();
            // 在module/build/generated/source/apt 生成
            javaFile.writeTo(annotationProcessor.getFileCreator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
