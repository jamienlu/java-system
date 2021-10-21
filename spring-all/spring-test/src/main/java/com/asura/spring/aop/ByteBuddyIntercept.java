package com.asura.spring.aop;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class ByteBuddyIntercept {
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
        System.out.println("before-invoke method:" + method.getName());
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
        System.out.println("after-invoke method return:" + ret.toString());
    }
}
