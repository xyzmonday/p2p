package com.yff.base.domain;

import com.yff.base.common.BidConst;

import java.util.Date;

public class IpLog extends BaseDomain{
    //登陆ip
    private String ip;
    //登陆状态
    private Byte loginState = BidConst.IP_STATE_SUCCESS;
    //用户名
    private String username;
    //用户登陆id
    private Long loginInfoId;
    //用户类型
    private Byte loginType;
    //登陆时间
    private Date loginTime;


    public String getStateDisplay() {
        return loginState == BidConst.IP_STATE_SUCCESS ? "登陆成功" : "登陆失败";
    }

    public String getUserTypeDisplay() {
        return loginType == BidConst.USER_CLIENT ? "前端用户" : "后端管理员";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Byte getLoginState() {
        return loginState;
    }

    public void setLoginState(Byte loginState) {
        this.loginState = loginState;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Long getLoginInfoId() {
        return loginInfoId;
    }

    public void setLoginInfoId(Long loginInfoId) {
        this.loginInfoId = loginInfoId;
    }

    public Byte getLoginType() {
        return loginType;
    }

    public void setLoginType(Byte loginType) {
        this.loginType = loginType;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}