package cn.hyy.leetov.annotation;

import cn.hyy.leetov.enums.LimitThresholdType;

import java.lang.annotation.*;

/**
 * 热键缓存方法
 *
 * @author HuYuanYang
 * @date 2024/03/24
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LHotKey {

    LimitThresholdType type() default LimitThresholdType.BY_QPS;

    int count() default 10;

    int duration();
}
