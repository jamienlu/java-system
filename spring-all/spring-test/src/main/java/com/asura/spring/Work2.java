package com.asura.spring;

import com.asura.spring.conf.Teacher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Import(Teacher.class)
public class Work2 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Object obj = context.getBean("student1"); // xml
        System.out.println(obj);
        Object obj2 = context.getBean("school1"); // 注解
        System.out.println(obj2);
        Object obj3 = context.getBean("teacher1"); // 注解
        System.out.println(obj3);
        Object obj4 = context.getBean("copyTeacher"); // 注解
        System.out.println(obj4);
    }


}
