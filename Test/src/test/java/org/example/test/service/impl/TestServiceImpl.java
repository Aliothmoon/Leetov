package org.example.test.service.impl;

import cn.hyy.leetov.annotation.LFallBack;
import org.example.test.service.TestComponent;
import org.example.test.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    private final TestComponent c;

    public TestServiceImpl(TestComponent service) {
        System.out.println(service);
        this.c = service;
    }
    int i = 0;
    @Override
    public String doService(String str) {
        return String.valueOf(i++);
    }

    public void doServiceFallback(String str) {
        System.out.println("Fallback");
    }

    @Override
    public String doService0(String str) {
        return "da";
    }

    @Override
    public String doService1(String str) {
        return null;
    }
}
