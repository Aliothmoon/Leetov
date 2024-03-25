package cn.hyy.leetov.processor.impl;

import cn.hyy.leetov.annotation.LFallBack;
import cn.hyy.leetov.enums.LTAnnotationType;
import cn.hyy.leetov.processor.Processor;
import cn.hyy.leetov.utils.SpecialClassUtils;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.MethodUtil;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FallbackProcessor implements Processor {
    private final Map<Method, Method> fallbackMethods = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FallbackProcessor.class);
    public static final String FALLBACK = "Fallback";

    @Override
    public Object invoke(Object original, Method method, Object[] args) throws Throwable {

        String resourceKey = MethodUtil.resolveMethodName(method);
        try (Entry entry = SphU.entry(resourceKey)) {
            return method.invoke(original, args);
        } catch (BlockException e) {
            LFallBack annotation = LTAnnotationType.getAnnotation(method);
            String fallbackName = Option.of(annotation).map(LFallBack::fallback).map(name -> {
                if (name.isEmpty()) {
                    return method.getName().concat(FALLBACK);
                }
                return name;
            }).get();
            Method fallbackMethod = fallbackMethods.computeIfAbsent(method, $ -> {
                Method i = SpecialClassUtils
                        .getFirstMethod(original.getClass(), m -> m.getName().equals(fallbackName));
                if (i == null) {
                    throw new IllegalStateException("Can't Find Fallback Method " + method);
                }
                return i;
            });
            try {
                return fallbackMethod.invoke(original, args);
            } catch (Exception ex) {
                log.info("check method signature is correct ?", ex);
                throw ex;
            }
        }
    }
}
