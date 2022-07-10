package com.asura.javcourse.jvm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class JVMClassLoaderDemo extends ClassLoader {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        MyClassLorder myClassLorder = new MyClassLorder();
        Class clazz = myClassLorder.loadMyClass("Hello.xlass");
        clazz.getDeclaredMethod("hello",null).invoke(clazz.getDeclaredConstructor().newInstance());
        clazz = null;
        Thread.sleep(1000);
        System.out.println(1);
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            a.add(i);
        }
        a =null;
        System.gc();
        Thread.sleep(10000);

    }




}
