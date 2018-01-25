package com.yff.base.query;

import java.util.Date;

import com.yff.base.common.DateUtil;
import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;


//分页查询的条件
public class IpLogQueryObject extends QueryObject {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    //登陆状态
    private int state = -1;
    //用户类型
    private int userType = -1;
    //用户名
    private String username;

    //获取到endDate时间的最后一秒
    public Date getEndDate() {
        return endDate == null ? null : DateUtil.endOfDay(endDate);
    }

    public String getUsername() {
        return StringUtils.hasLength(username) ? username : null;
    }


    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
