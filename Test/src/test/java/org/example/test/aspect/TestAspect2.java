package org.example.test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
//@Component
//@Order(Integer.MIN_VALUE)
public class TestAspect2 {
    private static final Logger log = LoggerFactory.getLogger(TestAspect2.class);



    @Around("execution(* org.example.test.service.TestService.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        log.info("Start 2");
        Object ret = point.proceed(args);
        log.info("End 2");
        return ret;
    }
}
