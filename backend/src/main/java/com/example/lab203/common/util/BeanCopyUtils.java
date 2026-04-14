package com.example.lab203.common.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean ??????
 */
public final class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    /**
     * ???????
     *
     * @param source ???
     * @param targetClass ????
     * @return ????
     * @param <T> ????
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception exception) {
            throw new IllegalStateException("Bean ????", exception);
        }
    }

    /**
     * ???????
     *
     * @param sourceList ???
     * @param targetClass ????
     * @return ????
     * @param <T> ????
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        return sourceList.stream().map(item -> copy(item, targetClass)).collect(Collectors.toList());
    }
}
