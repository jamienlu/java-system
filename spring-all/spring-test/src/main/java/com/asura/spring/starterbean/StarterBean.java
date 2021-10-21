package com.asura.spring.starterbean;

import com.asura.starter.bean.School;
import com.asura.starter.bean.Student;
import com.asura.starter.bean.Xlass;
import org.springframework.stereotype.Component;

;import javax.annotation.Resource;
@Component
public class StarterBean {
    @Resource
    private Student student;
    @Resource
    private Xlass xlass;
    @Resource
    private School school;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Xlass getXlass() {
        return xlass;
    }

    public void setXlass(Xlass xlass) {
        this.xlass = xlass;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "StarterBean{" +
                "student=" + student +
                ", xlass=" + xlass +
                ", school=" + school +
                '}';
    }
}
