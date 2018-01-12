package com.tdroid.processor;

import com.tdroid.AnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/21
 *      desc : 接口转发
 * </pre>
 */

public interface IProcessor {
    void process(RoundEnvironment roundEnv, AnnotationProcessor annotationProcessor);
}
