package com.yff.base.domain;

import com.alibaba.fastjson.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RechargeOffline extends BaseAuditDomain {

    //交易号
    private String tradeCode;
    //充值时间
    private Date tradeTime;
    //充值金额
    private BigDecimal amount;
    //充值说明
    private String note;
    //充值银行账号
    private CompanyBankInfo bankInfo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("username", this.getApplier().getUsername());
        json.put("tradeCode", tradeCode);
        json.put("tradeTime", tradeTime);
        json.put("amount", amount);
        json.put("note", note);
        return JSONObject.toJSONString(json);
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CompanyBankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(CompanyBankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }
}