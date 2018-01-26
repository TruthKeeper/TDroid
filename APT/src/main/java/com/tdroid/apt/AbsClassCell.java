package com.tdroid.apt;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/26
 *     desc   : 抽象的注解变量扫描后整理到类中
 * </pre>
 */
public abstract class AbsClassCell {
    /**
     * 类信息
     */
    public final TypeElement clsElement;

    public final Elements elementUtils;
    public final Filer fileCreator;
    public final Messager messager;
    public final Types typeUtils;
    /**
     * 存储类中的变量元素
     */
    public final List<VariableElement> elementList = new LinkedList<>();

    public AbsClassCell(TypeElement clsElement, Elements elementUtils, Filer fileCreator, Messager messager, Types typeUtils) {
        this.clsElement = clsElement;
        this.elementUtils = elementUtils;
        this.fileCreator = fileCreator;
        this.messager = messager;
        this.typeUtils = typeUtils;
    }

    public void addField(VariableElement element) {
        elementList.add(element);
    }

    /**
     * 生成代码
     */
    public abstract void generateCode();
}
