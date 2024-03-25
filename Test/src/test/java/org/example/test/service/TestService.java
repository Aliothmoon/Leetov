package org.example.test.service;

import cn.hyy.leetov.annotation.LFallBack;
import cn.hyy.leetov.annotation.LHotKey;
import cn.hyy.leetov.enums.LimitThresholdType;

public interface TestService {
    @LHotKey(type = LimitThresholdType.BY_QPS, count = 5, duration = 10)
    String doService(String str);

    @LFallBack(type = LimitThresholdType.BY_QPS, count = 5,fallback = "doService1")
    String doService0(String str);
    String doService1(String str);
}
