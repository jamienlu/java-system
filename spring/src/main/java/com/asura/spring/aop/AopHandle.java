package com.asura.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokaHandle implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object object = null;
        aop_Before(proxy);
        long time = System.currentTimeMillis();
        method.invoke(proxy, args);
        aop_After(time);
        return object;
    }
    private void aop_Before(Object realObj) {
        System.out.println("invoke object:" + realObj.getClass().getName());
    }

    private void aop_After(long time) {
        System.out.println("invoke cost time:" + (System.currentTimeMillis() - time) + "ms");
    }
}
