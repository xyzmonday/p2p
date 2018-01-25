package com.yff.base.service;

public interface IVerifyEmailService {
    /**
     * 发送绑定邮箱的邮件
     * @param email
     */
    void sendVerifyEmail(String email);
}
