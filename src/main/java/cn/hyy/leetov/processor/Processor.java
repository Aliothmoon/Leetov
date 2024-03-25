package cn.hyy.leetov.processor;

import java.lang.reflect.Method;

public interface Processor {
    Object invoke(Object original, Method method, Object[] args)
            throws Throwable;
}
