package com.yff.base.service.impl;

import com.yff.base.domain.RealAuth;
import com.yff.base.event.RealAuthSuccessEvent;
import com.yff.base.service.ISmsService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements ISmsService, ApplicationListener<RealAuthSuccessEvent> {
    @Override
    public void onRealAuthSendSms(RealAuth realAuth) {
        System.out.println("实名认证成功:" + realAuth.getRealName() + ";发送短信!!!!");
    }

    @Override
    public void onApplicationEvent(RealAuthSuccessEvent event) {
        this.onRealAuthSendSms(event.getRealAuth());
    }
}
