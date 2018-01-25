package com.yff.base.query;

import java.util.Date;

import com.yff.base.common.DateUtil;

import org.springframework.format.annotation.DateTimeFormat;


public class AuditQueryObject extends QueryObject {

    private int state = -1;
    private Date beginDate;
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    //获取到endDate时间的最后一秒
    public Date getEndDate() {
        return endDate == null ? null : DateUtil.endOfDay(endDate);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getBeginDate() {
        return beginDate;
    }
}
