package com.asura.javcourse.jvm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class JVMClassLoaderDemo extends ClassLoader {
    public static void main(String[] args) {
        InputStream inputStream = JVMClassLoaderDemo.class.getClassLoader().getResourceAsStream("Hello.xlass");
        JVMClassLoaderDemo loaderDemo = new JVMClassLoaderDemo();
        Class<?> clazz = loaderDemo.findClass(inputStream,"Hello");
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            if (method.getName().equals("hello")) {
                try {
                    method.invoke(clazz.getDeclaredConstructor().newInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final Class<?> findClass(InputStream inputStream, String name) {
        byte[] datas = input2byte(inputStream);
        byte[] target = new byte[datas.length];
        for (int i = 0; i < datas.length; i++) {
            target[i] = (byte) (255 - datas[i]);
        }
        if (datas != null) {
           return defineClass(name, target, 0, target.length);
        }
        return null;
    }

    private final byte[] input2byte(InputStream inputStream) {
        byte[] byteArray = null;
        try {
            int length = inputStream.available();
            byteArray = new byte[length];
            inputStream.read(byteArray);
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return byteArray;
        } finally {
            close(inputStream);
        }
    }

    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
