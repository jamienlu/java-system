package com.asura.database.conf.datasource;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceBeanName {
    String value() default "master";
}
