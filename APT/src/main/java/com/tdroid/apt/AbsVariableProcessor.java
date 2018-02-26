package com.tdroid.apt;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/26
 *     desc   : 抽象的注解变量扫描
 * </pre>
 */
public abstract class AbsVariableProcessor implements IProcessor {
    /**
     * 额外类全路径名-额外类
     */
    private final Map<String, AbsClassCell> classMap = new HashMap<>();

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor annotationProcessor) {
        //被注解修饰的集合
        Set<VariableElement> variableElements = ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(annotationClass()));
        for (VariableElement element : variableElements) {
            if (Utils.isPrivate(element) || Utils.isStatic(element)) {
                //过滤私有的和静态的
                continue;
            }
            //类信息
            TypeElement clsElement = (TypeElement) element.getEnclosingElement();
            //类全名
            String qualifiedName = clsElement.getQualifiedName().toString();
            AbsClassCell classCell = classMap.get(qualifiedName);
            if (classCell == null) {
                classCell = generateClassCell(clsElement,
                        annotationProcessor.getElementUtils(),
                        annotationProcessor.getFileCreator(),
                        annotationProcessor.getMessager(),
                        annotationProcessor.getTypeUtils());
                classMap.put(qualifiedName, classCell);
            }
            classCell.addField(element);
        }
        for (AbsClassCell clsExtra : classMap.values()) {
            //生成代码
            clsExtra.generateCode();
        }
    }

    public abstract Class<? extends Annotation> annotationClass();

    public abstract AbsClassCell generateClassCell(TypeElement clsElement, Elements elementUtils,
                                                   Filer fileCreator, Messager messager, Types typeUtils);
}

