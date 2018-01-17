package com.tdroid;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

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
    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    /**
     * @param element
     * @return
     */
    public static boolean isPrivate(Element element) {
        return element.getModifiers().contains(PRIVATE);
    }
    /**
     * @param element
     * @return
     */
    public static boolean isStatic(Element element) {
        return element.getModifiers().contains(STATIC);
    }
    /**
     * @param element
     * @return
     */
    public static boolean isAbstract(Element element) {
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

    public static String getBundleFieldType(String fieldType){
        switch (fieldType) {
            case "java.lang.String":return "String";
            case "java.lang.String[]":return "StringArray";
            case "java.util.ArrayList<java.lang.String>":return "StringArrayList";
            case "java.lang.Integer":case "int":return "Int";
            case "java.lang.Integer[]":case "int[]":return "IntArray";
            case "java.util.ArrayList<java.lang.Integer>": return "IntegerArrayList";
            case "java.lang.Short":case "short":return "Short";
            case "java.lang.Short[]":case "short[]":return "ShortArray";
            case "java.lang.Double":case "double":return "Double";
            case "java.lang.Double[]":case "double[]":return "DoubleArray";
            case "java.lang.Long":case "long":return "Long";
            case "java.lang.Long[]":case "long[]":return "LongArray";
            case "java.lang.Float":case "float":return "Float";
            case "java.lang.Float[]":case "float[]":return "FloatArray";
            case "java.lang.Boolean":case "boolean":return "Boolean";
            case "java.lang.Boolean[]":case "boolean[]":return "BooleanArray";
            case "java.lang.Byte":case "byte":return "Byte";
            case "java.lang.Byte[]":case "byte[]":return "ByteArray";
            case "java.lang.Character":case "char":return "Char";
            case "java.lang.Character[]":case "char[]":return "CharArray";
            case "java.lang.CharSequence":  return "CharSequence";
            case "java.lang.CharSequence[]":  return "CharSequenceArray";
            case "java.util.ArrayList<java.lang.CharSequence>":  return "CharSequenceArrayList";
            case "android.os.Bundle":  return "Bundle";
            case "java.io.Serializable":  return "Serializable";
            case "android.os.Parcelable":  return "Parcelable";
            case "android.os.Parcelable[]":  return "ParcelableArray";
            case "java.util.ArrayList<android.os.Parcelable>":  return "ParcelableArrayList";
        }
        return null;
    }
}
