package com.apt;

import com.apt.processor.InstanceProcessor;
import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/21
 *      desc : 处理注解
 * </pre>
 */
//生成 META-INF 信息
@AutoService(Processor.class)
//支持的源码版本
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//处理的注解
@SupportedAnnotationTypes({"com.apt.annotation.Instance"})
public class AnnotationProcessor extends AbstractProcessor {
    /**
     * 基于元素进行操作的工具方法
     */
    private Elements elementUtils;
    /**
     * 代码创建器
     */
    private Filer fileCreator;
    /**
     * 日志消息输出器
     */
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        fileCreator = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //转发
        new InstanceProcessor().process(roundEnvironment, this);
        return true;
    }

    public Elements getElementUtils() {
        return elementUtils;
    }

    public Filer getFileCreator() {
        return fileCreator;
    }

    public Messager getMessager() {
        return messager;
    }
}
