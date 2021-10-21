package com.asura.spring.starter.conf;

import com.asura.spring.starter.bean.Student;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("com.asura.xlass")
public class XlassProperties {
    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
