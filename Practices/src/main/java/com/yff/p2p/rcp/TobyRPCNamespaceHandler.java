package com.yff.p2p.rcp;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class TobyRPCNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        registerBeanDefinitionParser("reference", new TobyRPCBeanDefinitionParser());
    }
}

