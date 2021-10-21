package com.asura.spring.starter.bean;

import java.util.List;

public class Xlass {
    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Xlass{" +
                "students=" + students +
                '}';
    }
}
