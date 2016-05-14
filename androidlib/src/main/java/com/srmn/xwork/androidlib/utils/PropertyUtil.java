package com.srmn.xwork.androidlib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by kiler on 2016/5/14.
 */
public class PropertyUtil {


    public static <T, O> T getPropertyValue(String propertyName, Class<T> propertyType, O obj) throws NoSuchMethodException {

        Class classZ = obj.getClass();// 获取对象的类型

        Method m = classZ.getDeclaredMethod("get" + propertyName);

        try {
            T t = (T) m.invoke(obj, new Object[]{});// 调用方法获取方法的返回值
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

//    public  static <T> void setPropertyValue(T obj, String propertyName, Class<T> propertyType) throws NoSuchMethodException {
//
//        Class classZ = obj.getClass();// 获取对象的类型
//
//        Method m = classZ.getDeclaredMethod("set"+propertyName,propertyType);
//
//        try {
//            m.invoke(classZ, new Object[] {obj});// 调用方法获取方法的返回值
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
