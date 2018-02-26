package com.tdroid.apt;

import com.google.auto.service.AutoService;
import com.tdroid.apt.autoinject.AutoInjectProcessor;
import com.tdroid.apt.instace.InstanceProcessor;
import com.tdroid.apt.router.RouterProcessor;
import com.tdroid.apt.saverestore.SaveAndRestoreProcessor;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.tdroid.apt.AnnotationProcessor.MODULE_NAME;

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
@SupportedAnnotationTypes({
        //反射实例化
        "com.tdroid.annotation.Instance",
        //Activity Fragment 自动注入
        "com.tdroid.annotation.SaveAndRestore",
        //路由
        "com.tdroid.annotation.Router",
        //自动注入数据
        "com.tdroid.annotation.AutoInject"})
@SupportedOptions(value = {
        //组件化下的模块配置
        MODULE_NAME})
public class AnnotationProcessor extends AbstractProcessor {
    public static final String MODULE_NAME = "moduleName";
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
    /**
     * 配置的APT键值对
     */
    private Map<String, String> options;
    /**
     * 类型工具类
     */
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        fileCreator = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        options = processingEnv.getOptions();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //代替反射实例化的工具类
        new InstanceProcessor().process(roundEnvironment, this);
        //页面数据保存和恢复
        new SaveAndRestoreProcessor().process(roundEnvironment, this);
        //TRouter
        new RouterProcessor().process(roundEnvironment, this);
        //自动填充注入数据
        new AutoInjectProcessor().process(roundEnvironment, this);
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

    public Map<String, String> getOptions() {
        return options;
    }

    public Types getTypeUtils() {
        return typeUtils;
    }
}
