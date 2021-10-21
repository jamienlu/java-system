package com.asura.spring.resource;

import com.asura.spring.starter.bean.Student;
import org.springframework.stereotype.Component;

;import javax.annotation.Resource;
@Component
public class StarterBean {
    @Resource
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "StarterBean{" +
                "student=" + student +
                '}';
    }
}
