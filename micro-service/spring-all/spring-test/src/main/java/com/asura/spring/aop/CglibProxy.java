package com.asura.spring.aop;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {
    private Object target;

    public Object getProxyInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println(o.getClass().getName());
        System.out.println(method.getName());
        System.out.println(methodProxy.getSuperName());
        aop_Before(target);
        long begin = System.currentTimeMillis();
        //执行目标对象的方法
        Object returnValue = method.invoke(target, objects);
        aop_After(begin);
        return returnValue;
    }

    private void aop_Before(Object realObj) {
        System.out.println("before -- invoke object:" + realObj.getClass().getName());
    }

    private void aop_After(long time) {
        System.out.println("after -- invoke cost time:" + (System.currentTimeMillis() - time) + "ms");
    }
}
