package com.asura.spring.aop;

public class AopObjeChild implements AopObj {
    public String say() {
        System.out.println("hello world!");
        return "say";
    }
}
