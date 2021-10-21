package com.asura.spring;

import com.asura.spring.conf.SpringBootContextUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Work8 {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(Work8.class, args);
        Object obj = SpringBootContextUtil.getBean("starterBean"); // work8
        System.out.println(obj);
    }
}
