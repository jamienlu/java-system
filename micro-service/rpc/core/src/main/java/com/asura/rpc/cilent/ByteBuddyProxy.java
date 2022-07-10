package com.asura.rpc.cilent;

import com.asura.rpc.api.Filter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;


public class ByteBuddyProxy {
    public static <T> T  getProxyInstance(final Class<T> targetClass, String url, Filter... filters) {
        T obj = null;
        // 新类型的类名
        String childName = targetClass.getName()+ "_" + System.currentTimeMillis();
        try {
            obj = new ByteBuddy().subclass(targetClass)
                    .name(childName).method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(new ByteBuddyIntercept(targetClass, url, filters)))
                    .make().load(ByteBuddy.class.getClassLoader())
                    .getLoaded().newInstance();
            return obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
