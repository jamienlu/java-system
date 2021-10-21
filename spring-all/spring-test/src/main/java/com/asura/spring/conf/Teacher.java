package com.asura.spring.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component("teacher1")
public class Teacher {

    private String name = "teacher";
    private int age = 18;

    @Bean(name = "copyTeacher")
    public Teacher getTeacher() {
        Teacher teacher = new Teacher();
        teacher.setName("copy-teacher");
        return teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

}
