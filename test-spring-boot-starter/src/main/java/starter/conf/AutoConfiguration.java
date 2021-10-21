package com.asura.spring.starter.conf;

import com.asura.spring.starter.bean.School;
import com.asura.spring.starter.bean.Student;
import com.asura.spring.starter.bean.Xlass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Student.class, Xlass.class, School.class})
@EnableConfigurationProperties({StudentProperties.class, XlassProperties.class, SchoolProperties.class})
public class AutoConfiguration {
    @Bean
    public Student createStudent(StudentProperties studentProperties) {
        Student student = new Student();
        student.setId(studentProperties.getId());
        student.setName(studentProperties.getName());
        return student;
    }

    @Bean
    public Xlass createXlass(XlassProperties xlassProperties) {
        Xlass xlass = new Xlass();
        xlass.setStudents(xlassProperties.getStudents());
        return xlass;
    }

    @Bean
    public School createSchool(SchoolProperties schoolProperties) {
        School school = new School();
        school.setStudent(schoolProperties.getStudent());
        school.setXlass(schoolProperties.getXlass());
        return school;
    }
}
