package cn.hyy.leetov.rule;

import cn.hyy.leetov.annotation.LFallBack;
import cn.hyy.leetov.annotation.LHotKey;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.util.MethodUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RulesHelper {


    private static final List<FlowRule> flowRules = new ArrayList<>();

    private RulesHelper() {
    }


    public static void flowRule(LFallBack fallBack, Method method) {
        String resourceKey = MethodUtil.resolveMethodName(method);
        FlowRule rule = new FlowRule()
                .setGrade(fallBack.type().getGrade())
                .setResource(resourceKey)
                .as(FlowRule.class)
                .setCount(fallBack.count());
        flowRules.add(rule);
    }

    public static void endFlowRule() {
        FlowRuleManager.loadRules(flowRules);
    }

    public static void dynamicParameterFlowRule(LHotKey hotKey, String resourceKey) {
        if (!ParamFlowRuleManager.hasRules(resourceKey)) {
            ParamFlowRule rule = new ParamFlowRule();
            rule.setResource(resourceKey);
            rule.setGrade(hotKey.type().getGrade());
            rule.setCount(hotKey.count());
            rule.setDurationInSec(hotKey.duration());
            rule.setParamIdx(0);
            ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
        }
    }


}
