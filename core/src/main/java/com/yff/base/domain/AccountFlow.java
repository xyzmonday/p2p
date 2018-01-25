package com.yff.base.domain;

import java.math.BigDecimal;
import java.util.Date;

public class AccountFlow extends BaseDomain{
    private Long accountId;//本次流水记录的账号
    private BigDecimal amount;//本次流水记录的金额
    private Date tradeTime;//本次流水发生的时间
    private BigDecimal usableAmount;//账户可用金额
    //当投标时，该标处于满标二审之前，那么投资人的钱处于冻结状态
    //当取现时，没有审核通过之前，该钱属于冻结状态
    private BigDecimal freezedAmount;//账户冻结金额
    private Byte accountType;//资金变化类型
    private String note;//流水说明


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
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

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}