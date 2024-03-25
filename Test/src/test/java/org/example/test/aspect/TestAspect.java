package org.example.test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
//@Component
public class TestAspect {
    private static final Logger log = LoggerFactory.getLogger(TestAspect.class);



    @Around("execution(* org.example.test.service.TestService.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        log.info("Start");
        Object ret = point.proceed(args);
        log.info("End");
        return ret;
    }
}
