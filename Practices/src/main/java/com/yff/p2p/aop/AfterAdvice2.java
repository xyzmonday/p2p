package com.yff.p2p.aop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class AfterAdvice2 implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method,
            Object[] args, Object target) throws Throwable {  
        System.out.println("AfterAdvice2.afterReturning() execute ");  
          
    }  
}  