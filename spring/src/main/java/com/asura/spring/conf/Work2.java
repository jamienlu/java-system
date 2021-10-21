package com.asura.spring.conf;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanConf {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Object obj = context.getBean("student");
        System.out.println(obj);
        Object obj = context.getBean("student");
        System.out.println(obj);
    }
}
