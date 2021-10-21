package com.asura.starter.conf;

import com.asura.starter.bean.Student;
import com.asura.starter.bean.Xlass;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.asura.school")
public class SchoolProperties {
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
}
