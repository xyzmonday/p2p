package com.yff.base.service.impl;

import com.yff.base.domain.RealAuth;
import com.yff.base.event.RealAuthSuccessEvent;
import com.yff.base.service.IEmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 发送邮件服务
 */
@Service
public class EmailServiceImpl implements IEmailService, ApplicationListener<RealAuthSuccessEvent> {

    @Override
    public void onRealAuthSendEmail(RealAuth realAuth) {
        System.out.println("实名认证成功:" + realAuth.getRealName() + ";发送邮件!!!!");
    }

    @Override
    public void onApplicationEvent(RealAuthSuccessEvent event) {
        this.onRealAuthSendEmail(event.getRealAuth());
    }
}
