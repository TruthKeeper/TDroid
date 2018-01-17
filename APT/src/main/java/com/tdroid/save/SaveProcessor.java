package com.tdroid.save;

import com.tdroid.AnnotationProcessor;
import com.tdroid.IProcessor;
import com.tdroid.Utils;
import com.tdroid.annotation.Save;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;


/**
 * <pre>
 *      author : TK
 *      time : 2018/1/16
 *      desc :
 * </pre>
 */

public class SaveProcessor implements IProcessor {
    /**
     * 额外类全路径名-额外类
     */
    private final Map<String, SaveClassExtra> classMap = new HashMap<>();

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor annotationProcessor) {
        //被注解修饰的集合
        Set<VariableElement> variableElements = ElementFilter.fieldsIn(roundEnv.getElementsAnnotatedWith(Save.class));
        for (VariableElement element : variableElements) {
            annotationProcessor.getMessager().printMessage(Diagnostic.Kind.NOTE, "解析：" + element.toString());
            if (Utils.isPrivate(element) || Utils.isStatic(element)) {
                //过滤非有效域
                continue;
            }
            //类信息
            TypeElement clsElement = (TypeElement) element.getEnclosingElement();
            String qualifiedName = clsElement.getQualifiedName().toString();
            SaveClassExtra saveClassExtra = classMap.get(qualifiedName);
            if (saveClassExtra == null) {
                saveClassExtra = new SaveClassExtra(clsElement,
                        annotationProcessor.getElementUtils(),
                        annotationProcessor.getFileCreator(),
                        annotationProcessor.getMessager());
                classMap.put(qualifiedName, saveClassExtra);
            }
            saveClassExtra.addField(element);
        }
        for (SaveClassExtra clsExtra : classMap.values()) {
            clsExtra.generateCode();
        }
    }
}
