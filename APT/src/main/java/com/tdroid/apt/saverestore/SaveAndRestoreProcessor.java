package com.tdroid.apt.saverestore;

import com.tdroid.apt.AbsClassCell;
import com.tdroid.apt.AbsVariableProcessor;
import com.tdroid.annotation.SaveAndRestore;

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

public class SaveAndRestoreProcessor extends AbsVariableProcessor {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return SaveAndRestore.class;
    }

    @Override
    public AbsClassCell generateClassCell(TypeElement clsElement, Elements elementUtils, Filer fileCreator, Messager messager, Types typeUtils) {
        return new SaveRestoreClassCell(clsElement, elementUtils, fileCreator, messager,typeUtils);
    }

}
