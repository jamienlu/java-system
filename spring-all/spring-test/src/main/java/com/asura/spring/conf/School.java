package com.asura.spring.conf;

import org.springframework.stereotype.Component;

@Component("school1")
public class School {
    private String name;
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }

    public Student selectStuent() {
        System.out.println("selectStuent");
        return new Student();
    }
}
