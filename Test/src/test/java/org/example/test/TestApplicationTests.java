package org.example.test;

import cn.hyy.leetov.proxy.LimitInvocationHandler;
import org.example.test.service.TestService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestApplicationTests {
    @Autowired
    TestService service;
    private static final Logger log = LoggerFactory.getLogger(TestApplicationTests.class);

    @Test
    void contextLoads() {
        for (int i = 0; i < 10; i++) {
            try {
                log.info(service.doService("A"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    void contextLoads2() {
        for (int i = 0; i < 10; i++) {
            try {
                log.info(service.doService0("A"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
