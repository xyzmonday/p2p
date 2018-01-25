package com.yff.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 绑定邮箱验证的对象
 */
public class MailVerify extends BaseDomain{
    private String uuid;
    private String email;
    //发送邮件的人
    private Long loginInfoId;
    //发送时间
    private Date sendTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLoginInfoId() {
        return loginInfoId;
    }

    public void setLoginInfoId(Long loginInfoId) {
        this.loginInfoId = loginInfoId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
