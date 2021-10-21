package com.asura.spring.aop;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyProxy {
    public Object getProxyInstance(Object targetObj) {
        Object t = null;
        String childName = targetObj.getClass().getName()+ "_" + System.currentTimeMillis(); // 新类型的类名
        try {
            t = new ByteBuddy().subclass(targetObj.getClass())
                    .name(childName).method(ElementMatchers.any())
                    .intercept(Advice.to(ByteBuddyIntercept.class))
                    .make().load(ByteBuddy.class.getClassLoader())
                    .getLoaded().newInstance();
            return t;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }
}
