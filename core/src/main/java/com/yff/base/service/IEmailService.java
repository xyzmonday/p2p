package com.yff.base.service;

import com.yff.base.domain.RealAuth;

public interface IEmailService {
    /**
     * 实名认证成功后发送邮件
     * @param realAuth
     */
    void onRealAuthSendEmail(RealAuth realAuth);
}
