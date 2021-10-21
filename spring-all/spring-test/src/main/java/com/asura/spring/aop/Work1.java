package com.asura.spring.aop;
public class Work1 {
    public static void main(String[] args) {
        Object obj = new AopHandle().createProxy(new AopObjeChild());
        AopObj aopObj = (AopObj)obj;
        aopObj.say();

        Object obj2 = new CglibProxy().getProxyInstance(new AopObjeChild());
        AopObj aopObj2 = (AopObj)obj2;
        aopObj2.say();

        Object obj3 = new ByteBuddyProxy().getProxyInstance(new AopObjeChild());
        AopObj aopObj3 = (AopObj)obj3;
        aopObj3.say();
    }
}
