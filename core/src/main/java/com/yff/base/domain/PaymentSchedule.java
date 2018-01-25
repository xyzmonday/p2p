package com.yff.base.domain;

import com.yff.base.common.BidConst;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentSchedule {
    private Long id;
    // 本期还款截止期限
    private Date deadLine;
    //还款日期
    private Date payDate;
    // 本期还款总金额，利息 +本金
    private BigDecimal totalAmount = BidConst.ZERO;
    // 本期还款本金
    private BigDecimal principal  = BidConst.ZERO;
    // 本期还款总利息
    private BigDecimal interest = BidConst.ZERO;
    // 第几期 (即第几个月)
    private Byte monthIndex;
    // 本期还款状态（默认正常待还）
    private Byte state = BidConst.PAYMENT_STATE_NORMAL;
    // 借款类型
    private Byte bidRequestType;
    // 还款方式，等同借款(BidRequest)中的还款方式
    private Byte returnType;
    // 对应借款
    private Long bidRequestId;
    // 还款人
    private LoginInfo borrowUser;
    private Long bidUserId;
    //借款名称
    private String bidRequestTitle;
    // 本期还款计划对应的还款计划明细
    private List<PaymentScheduleDetail> paymentScheduleDetails = new ArrayList<PaymentScheduleDetail>();

    public String getStateDisplay() {
        switch (state) {
            case BidConst.PAYMENT_STATE_NORMAL:
                return "正常待还";
            case BidConst.PAYMENT_STATE_DONE:
                return "已还";
            case BidConst.PAYMENT_STATE_OVERDUE:
                return "逾期";
            default:
                return "未知";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Byte getMonthIndex() {
        return monthIndex;
    }

    public void setMonthIndex(Byte monthIndex) {
        this.monthIndex = monthIndex;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getBidRequestType() {
        return bidRequestType;
    }

    public void setBidRequestType(Byte bidRequestType) {
        this.bidRequestType = bidRequestType;
    }

    public Byte getReturnType() {
        return returnType;
    }

    public void setReturnType(Byte returnType) {
        this.returnType = returnType;
    }

    public Long getBidRequestId() {
        return bidRequestId;
    }

    public void setBidRequestId(Long bidRequestId) {
        this.bidRequestId = bidRequestId;
    }

    public LoginInfo getBorrowUser() {
        return borrowUser;
    }

    public void setBorrowUser(LoginInfo borrowUser) {
        this.borrowUser = borrowUser;
    }

    public Long getBidUserId() {
        return bidUserId;
    }

    public void setBidUserId(Long bidUserId) {
        this.bidUserId = bidUserId;
    }

    public String getBidRequestTitle() {
        return bidRequestTitle;
    }

    public void setBidRequestTitle(String bidRequestTitle) {
        this.bidRequestTitle = bidRequestTitle;
    }

    public List<PaymentScheduleDetail> getPaymentScheduleDetails() {
        return paymentScheduleDetails;
    }

    public void setPaymentScheduleDetails(List<PaymentScheduleDetail> paymentScheduleDetails) {
        this.paymentScheduleDetails = paymentScheduleDetails;
    }
}