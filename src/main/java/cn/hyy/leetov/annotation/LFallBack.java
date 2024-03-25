package cn.hyy.leetov.annotation;

import cn.hyy.leetov.enums.LimitThresholdType;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LFallBack {
    /**
     * 熔断方法 默认为原方法名+FallBack
     *
     * @return {@code String}
     */
    String fallback() default "";

    LimitThresholdType type() default LimitThresholdType.BY_THREAD;

    int count() default 10;
}
