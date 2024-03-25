package cn.hyy.leetov.proxy;

import cn.hyy.leetov.enums.LTAnnotationType;
import cn.hyy.leetov.processor.Processor;
import cn.hyy.leetov.utils.SpecialClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class LimitInvocationHandler implements InvocationHandler {
    private final Object original;
    private static final Logger log = LoggerFactory.getLogger(LimitInvocationHandler.class);

    public LimitInvocationHandler(Object original) {
        this.original = original;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (SpecialClassUtils.isObjectMethod(method.getName())) {
            return method.invoke(original, args);
        }


        if (LTAnnotationType.check(method)) {
            Annotation annotation = LTAnnotationType.getAnnotation(method);

            Processor processor = LTAnnotationType.of(annotation);
            return processor.invoke(original, method, args);
        }
        return method.invoke(original, args);
    }
}