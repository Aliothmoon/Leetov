package cn.hyy.leetov.enums;

import cn.hyy.leetov.annotation.LFallBack;
import cn.hyy.leetov.annotation.LHotKey;
import cn.hyy.leetov.processor.impl.FallbackProcessor;
import cn.hyy.leetov.processor.impl.HotKeyCacheProcessor;
import cn.hyy.leetov.processor.Processor;
import com.alibaba.csp.sentinel.util.MethodUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public enum LTAnnotationType {
    LT_HOTKEY(new HotKeyCacheProcessor()),
    LT_FALLBACK(new FallbackProcessor());


    private final Processor processor;
    private static final Map<String, Annotation> RESOURCES_KEY = new HashMap<>();

    public static void put(Method m, Annotation type) {
        String resourceKey = MethodUtil.resolveMethodName(m);
        RESOURCES_KEY.put(resourceKey, type);
    }

    public static boolean check(Method m) {
        return RESOURCES_KEY.containsKey(MethodUtil.resolveMethodName(m));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Method m) {
        return (T) RESOURCES_KEY.get(MethodUtil.resolveMethodName(m));
    }

    public static Processor of(Annotation anno) {
        if (anno instanceof LHotKey) {
            return LT_HOTKEY.processor;
        } else if (anno instanceof LFallBack) {
            return LT_FALLBACK.processor;
        } else {
            throw new IllegalStateException("Can't Use this Annotation");
        }
    }


    LTAnnotationType(Processor processor) {
        this.processor = processor;
    }
}
