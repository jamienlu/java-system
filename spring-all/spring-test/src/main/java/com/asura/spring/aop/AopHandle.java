package com.asura.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopHandle implements InvocationHandler {
    private Object realObj;

    public Object createProxy(Object realObj) {
        this.realObj = realObj;
        return Proxy.newProxyInstance(realObj.getClass().getClassLoader(), realObj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy class:" + proxy.getClass().getName());
        Object object = null;
        aop_Before(realObj);
        long time = System.currentTimeMillis();
        method.invoke(realObj, args);
        aop_After(time);
        return object;
    }

    private void aop_Before(Object realObj) {
        System.out.println("before -- invoke object:" + realObj.getClass().getName());
    }

    private void aop_After(long time) {
        System.out.println("after -- invoke cost time:" + (System.currentTimeMillis() - time) + "ms");
    }
}
