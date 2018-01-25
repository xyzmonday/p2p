package com.yff.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Bid extends BaseDomain{

    // 年化利率(等同于bidRequest上的currentRate)
    private BigDecimal actualRate;
    // 本次投标金额
    private BigDecimal availableAmount;
    //借款标关联id
    private Long bidRequestId;
    //投标人
    private LoginInfo bidUser;

    private Date bidTime;

    private String bidRequestTitle;

    //不保存到数据库，仅仅查询使用
    private int bidRequestState;

    public int getBidRequestState() {
        return bidRequestState;
    }

    public void setBidRequestState(int bidRequestState) {
        this.bidRequestState = bidRequestState;
    }

    public BigDecimal getActualRate() {
        return actualRate;
    }

    public void setActualRate(BigDecimal actualRate) {
        this.actualRate = actualRate;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Long getBidRequestId() {
        return bidRequestId;
    }

    public void setBidRequestId(Long bidRequestId) {
        this.bidRequestId = bidRequestId;
    }

    public LoginInfo getBidUser() {
        return bidUser;
    }

    public void setBidUser(LoginInfo bidUser) {
        this.bidUser = bidUser;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public String getBidRequestTitle() {
        return bidRequestTitle;
    }

    public void setBidRequestTitle(String bidRequestTitle) {
        this.bidRequestTitle = bidRequestTitle == null ? null : bidRequestTitle.trim();
    }
}