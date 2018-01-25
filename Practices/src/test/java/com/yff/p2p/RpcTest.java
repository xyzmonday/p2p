package com.yff.p2p;

import com.yff.p2p.rcp.IHelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class RpcTest {

    @Autowired
    private IHelloService helloService;

    @Test
    public void test() {
        helloService.sayHello();
    }


}
