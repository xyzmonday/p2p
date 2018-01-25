package com.yff.base.domain;

import com.yff.base.common.BidConst;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private String tradePassword;
    //可用余额
    private BigDecimal usableAmount = BidConst.ZERO;
    //冻结金额
    private BigDecimal freezedAmount = BidConst.ZERO;
    private Integer version;
    //待收利息
    private BigDecimal unReceiveInterest = BidConst.ZERO;
    //待收本金
    private BigDecimal unReceivePrincipal = BidConst.ZERO;
    //带还本金
    private BigDecimal unReturnAmount = BidConst.ZERO;
    //可借金额
    private BigDecimal remainBorrowLimit = BidConst.INIT_BORROW_LIMIT;
    //借款最大金额
    private BigDecimal borrowLimit = BidConst.INIT_BORROW_LIMIT;
    /**
     * 获取用户余额。包括了可用余额+冻结金额+待收本金
     * @return
     */
    public BigDecimal getTotalAmount() {
        return usableAmount.add(freezedAmount).add(unReceivePrincipal);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradePassword() {
        return tradePassword;
    }

    public void setTradePassword(String tradePassword) {
        this.tradePassword = tradePassword == null ? null : tradePassword.trim();
    }

    public BigDecimal getUsableAmount() {
        return usableAmount;
    }

    public void setUsableAmount(BigDecimal usableAmount) {
        this.usableAmount = usableAmount;
    }

    public BigDecimal getFreezedAmount() {
        return freezedAmount;
    }

    public void setFreezedAmount(BigDecimal freezedAmount) {
        this.freezedAmount = freezedAmount;
    }

    public BigDecimal getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(BigDecimal borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getUnReceiveInterest() {
        return unReceiveInterest;
    }

    public void setUnReceiveInterest(BigDecimal unReceiveInterest) {
        this.unReceiveInterest = unReceiveInterest;
    }

    public BigDecimal getUnReceivePrincipal() {
        return unReceivePrincipal;
    }

    public void setUnReceivePrincipal(BigDecimal unReceivePrincipal) {
        this.unReceivePrincipal = unReceivePrincipal;
    }

    public BigDecimal getUnReturnAmount() {
        return unReturnAmount;
    }

    public void setUnReturnAmount(BigDecimal unReturnAmount) {
        this.unReturnAmount = unReturnAmount;
    }

    public BigDecimal getRemainBorrowLimit() {
        return remainBorrowLimit;
    }

    public void setRemainBorrowLimit(BigDecimal remainBorrowLimit) {
        this.remainBorrowLimit = remainBorrowLimit;
    }
}