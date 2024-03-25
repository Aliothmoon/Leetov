package cn.hyy.leetov.config;

import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LTConfigProperties.class)
public class LTSpringAutoConfiguration {

    public static LTConfigProperties LT_PROPERTIES;

    @Bean
    public InstantiationAwareBeanPostProcessor postProcessConfiguration(LTConfigProperties properties) {
        LTSpringAutoConfiguration.LT_PROPERTIES = properties;
        return new LTBeanPostProcessor();
    }
}
