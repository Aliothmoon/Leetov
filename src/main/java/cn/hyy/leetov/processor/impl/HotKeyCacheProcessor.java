package cn.hyy.leetov.processor.impl;

import cn.hyy.leetov.annotation.LHotKey;
import cn.hyy.leetov.enums.LTAnnotationType;
import cn.hyy.leetov.processor.Processor;
import cn.hyy.leetov.rule.RulesHelper;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.alibaba.fastjson2.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static cn.hyy.leetov.config.LTSpringAutoConfiguration.LT_PROPERTIES;

public class HotKeyCacheProcessor implements Processor {
    private static final Cache<String, Object> cache = Caffeine
            .newBuilder()
            .expireAfterWrite(LT_PROPERTIES.getCacheTime(), TimeUnit.SECONDS)
            .softValues()
            .build();

    @Override
    public Object invoke(Object original, Method method, Object[] args) throws Throwable {
        LHotKey key = LTAnnotationType.getAnnotation(method);
        String cacheParameter = JSON.toJSONString(args);
        String resourceKey = MethodUtil.resolveMethodName(method);
        RulesHelper.dynamicParameterFlowRule(key, resourceKey);


        try (Entry entry = SphU.entry(resourceKey, EntryType.IN, 1, cacheParameter)) {
            return method.invoke(original, args);
        } catch (BlockException e) {
            return cache.get(cacheParameter, s -> {
                try {
                    return method.invoke(original, args);
                } catch (Exception re) {
                    throw new RuntimeException("invoke error",re);
                }
            });
        }
    }
}
