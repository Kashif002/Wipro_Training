package com.example.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.aopdemo.service.StudentService.enroll(..))")
    public void beforeLogger(JoinPoint jp) {
        System.out.println("Before Logger: About to call " + jp.getSignature().getName());
    }

    @Around("execution(* com.example.aopdemo.service.StudentService.getStudent(..))")
    public Object aroundLogger(ProceedingJoinPoint jp) throws Throwable {
        Object arg = jp.getArgs()[0];
        System.out.println("Around Logger: Argument passed = " + arg);
        Object result = jp.proceed();
        System.out.println("Around Logger: Result = " + result);
        return result;
    }

    @AfterReturning(pointcut="execution(* com.example.aopdemo.service.StudentService.enroll(..))", returning = "returnString")
    public void getNameReturningAdvice(JoinPoint jp, String returnString) {
        System.out.println("AfterReturning Logger: Returned string = " + returnString);
    }

    @AfterThrowing("execution(* com.example.aopdemo.service.StudentService.throwError(..))")
    public void logAfterThrowing(JoinPoint jp) {
        System.out.println("AfterThrowing Logger: Exception thrown in " + jp.getSignature().getName());
    }

    @After("execution(* com.example.aopdemo.service.StudentService.enroll(..))")
    public void afterLogger(JoinPoint jp) {
        Object arg = jp.getArgs()[0];
        System.out.println("After Logger: Method " + jp.getSignature().getName() + " executed with argument: " + arg);
    }
}