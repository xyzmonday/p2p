package com.yff.p2p;

import com.yff.p2p.aop.*;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;

public class AopTest {

    @Test
    public void proxyLoginTest() {
        ProxyFactory factory = new ProxyFactory();
        factory.addAdvice(new BeforeAdvice1());
        factory.addAdvice(new BeforeAdvice2());
        factory.addAdvice(new AfterAdvice1());
        factory.addAdvice(new AfterAdvice2());
        LoginService target = new LoginServiceImpl();
        factory.setTarget(target);
        factory.setProxyTargetClass(false);
        factory.setInterfaces(new Class[]{LoginService.class});

        LoginService service = (LoginService)factory.getProxy();
        System.out.println(service.sayHello());
    }
}
