package com.asura.spring.starter.bean;

public class School {
    private Xlass xlass;
    private Student student;

    public Xlass getXlass() {
        return xlass;
    }

    public void setXlass(Xlass xlass) {
        this.xlass = xlass;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "School{" +
                "xlass=" + xlass +
                ", student=" + student +
                '}';
    }
}
