package com.tdroid;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * <pre>
 *      author : TK
 *      time : 2017/9/21
 *      desc :
 * </pre>
 */
public final class Utils {
    public static final String PACKAGENAME = "com.apt";

    /**
     * @param element
     * @return
     */
    public static boolean isPublic(TypeElement element) {
        return element.getModifiers().contains(PUBLIC);
    }

    /**
     * @param element
     * @return
     */
    public static boolean isAbstract(TypeElement element) {
        return element.getModifiers().contains(ABSTRACT);
    }

    /**
     * 是否是一个确实的class
     *
     * @param messager
     * @param element
     * @return
     */
    public static boolean isValidClass(Messager messager, TypeElement element) {
        if (element.getKind() != ElementKind.CLASS) {
            messager.printMessage(Diagnostic.Kind.ERROR, "must be class !", element);
            return false;
        }

        if (!isPublic(element)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "must be public !", element);
            return false;
        }

        if (isAbstract(element)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "must not be abstract !", element);
            return false;
        }
        return true;
    }

}
