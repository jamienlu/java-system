package com.asura.spring.starter.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.asura.student")
public class StudentProperties {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
