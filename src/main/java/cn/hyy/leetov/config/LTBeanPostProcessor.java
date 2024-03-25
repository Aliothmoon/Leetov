package cn.hyy.leetov.config;

import cn.hyy.leetov.annotation.LFallBack;
import cn.hyy.leetov.annotation.LHotKey;
import cn.hyy.leetov.enums.LTAnnotationType;
import cn.hyy.leetov.proxy.LimitProxyExecutor;
import cn.hyy.leetov.rule.RulesHelper;
import cn.hyy.leetov.utils.SpecialClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

@AutoConfiguration
public class LTBeanPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(LTBeanPostProcessor.class);


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clz = ClassUtils.getUserClass(bean);
        boolean[] ref = new boolean[]{false};
        SpecialClassUtils.getAllMethods(clz).forEach(m -> {
            LHotKey lhotKey;
            LFallBack fallBackKey;
            if ((lhotKey = AnnotationUtils.findAnnotation(m, LHotKey.class)) != null) {
                LTAnnotationType.put(m, lhotKey);
                ref[0] = true;
            } else if ((fallBackKey = AnnotationUtils.findAnnotation(m, LFallBack.class)) != null) {
                RulesHelper.flowRule(fallBackKey, m);
                LTAnnotationType.put(m, fallBackKey);
                ref[0] = true;
            }

        });
        if (ref[0]) {
            return new LimitProxyExecutor(bean, clz).make();
        }

        return bean;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RulesHelper.endFlowRule();
    }


}
