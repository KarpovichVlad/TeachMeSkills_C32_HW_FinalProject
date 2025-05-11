package org.example.teachmeskills_c32_hw_finalproject.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class LogAspect {

    @Before("@annotation(org.example.teachmeskills_c32_hw_finalproject.annotation.LogAnnotation)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(new Date() + " INFO IN:" + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName());
    }

    @After("@annotation(org.example.teachmeskills_c32_hw_finalproject.annotation.LogAnnotation)")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println(new Date() + " INFO OUT:" + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName());
    }

    @AfterThrowing(value = "@annotation(org.example.teachmeskills_c32_hw_finalproject.annotation.LogAnnotation)", throwing = "err")
    public void logAfterThrowing(JoinPoint joinPoint, Exception err) {
        System.out.println(new Date() + " ERROR " + joinPoint.getSignature().getDeclaringType() + ":"
                + joinPoint.getSignature().getName() + ":" + err.getMessage());
    }
}

