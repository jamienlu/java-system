package com.asura.database.conf.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
@Aspect
@Slf4j
public class DynamicDataSourceAop {
    @Around("execution(public * com.asura.database.dao..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        String targetDataSource = "master";
        if(targetMethod.isAnnotationPresent(DataSourceBeanName.class)){
            targetDataSource = targetMethod.getAnnotation(DataSourceBeanName.class).value() ;
            DataSourceContext.setDataSource(targetDataSource);
        }
        log.info("DynamicDataSourceAop get targetDataSource:" + targetDataSource);
        Object result = pjp.proceed();
        DataSourceContext.clearDataSource();
        log.info("DynamicDataSourceAop clear targetDataSource:" + targetDataSource);
        return result;
    }
}
