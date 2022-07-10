package com.asura.druid;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class DruidAppliction {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(DruidAppliction.class, args);
    }
}
