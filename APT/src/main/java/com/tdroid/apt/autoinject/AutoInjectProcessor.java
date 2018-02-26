package com.tdroid.apt.autoinject;

import com.tdroid.apt.AbsClassCell;
import com.tdroid.apt.AbsVariableProcessor;
import com.tdroid.annotation.AutoInject;

import java.lang.annotation.Annotation;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


/**
 * <pre>
 *      author : TK
 *      time : 2018/1/16
 *      desc :
 * </pre>
 */

public class AutoInjectProcessor extends AbsVariableProcessor {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoInject.class;
    }

    @Override
    public AbsClassCell generateClassCell(TypeElement clsElement, Elements elementUtils, Filer fileCreator, Messager messager, Types typeUtils) {
        return new AutoInjectClassExtra(clsElement, elementUtils, fileCreator, messager, typeUtils);
    }
}
