package io.renren.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author huyan
 * @date 2024/3/11
 */
public class BaseBeanUtils {


    public BaseBeanUtils() {
    }

    public static void map(Object source, Object target) {
        try {
            BeanUtils.copyProperties(source, target);
        } catch (Exception var3) {
            throw new RuntimeException("拷贝对象属性失败", var3);
        }
    }


    public static <T> T map(Object source, Class<T> target) {
        if (target != null && !ObjectUtils.isEmpty(source)) {
            try {
                T t = target.newInstance();
                map(source, t);
                return t;
            } catch (Exception var3) {
                throw new RuntimeException("拷贝对象属性失败", var3);
            }
        } else {
            return null;
        }
    }

    public static <T> T map(Object source, Class<T> target, String... ignore) {
        if (target != null && !ObjectUtils.isEmpty(source)) {
            try {
                T t = target.newInstance();
                map(source, t);
                return t;
            } catch (Exception var4) {
                throw new RuntimeException("拷贝对象属性失败", var4);
            }
        } else {
            return null;
        }
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList();
        if (CollectionUtils.isEmpty(sourceList)) {
            return targetList;
        } else {
            Iterator var5 = sourceList.iterator();

            while (var5.hasNext()) {
                Object sourObj = var5.next();

                T tarObj;
                try {
                    tarObj = targetClass.newInstance();
                } catch (InstantiationException | IllegalAccessException var9) {
                    throw new RuntimeException("拷贝对象属性失败", var9);
                }

                map(sourObj, tarObj);
                targetList.add(tarObj);
            }

            return targetList;
        }
    }

    private static List<Field> getAllField(Object model) {
        Class<?> clazz = model.getClass();

        ArrayList fields;
        for (fields = new ArrayList(); clazz != null; clazz = clazz.getSuperclass()) {
            fields.addAll(new ArrayList(Arrays.asList(clazz.getDeclaredFields())));
        }

        return fields;
    }

}
