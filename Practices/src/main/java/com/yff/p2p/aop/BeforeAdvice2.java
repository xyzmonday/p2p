package com.yff.p2p.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class BeforeAdvice2 implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target)
            throws Throwable {  
        System.out.println("BeforeAdvice2.before() execute ");  
    }  
     
}  