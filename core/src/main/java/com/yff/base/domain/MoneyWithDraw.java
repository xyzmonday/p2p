package com.yff.base.domain;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoneyWithDraw extends BaseAuditDomain{

    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankForkName;
    private BigDecimal amount;
    //本次提现金额
    private BigDecimal charge;

    public String getJsonString (){
        Map<String , Object> json =  new HashMap<>();
        json.put("id", id) ;
        json.put("username", this.applier.getUsername()) ;
        json.put("realName", accountName) ;
        json.put("applyTime", applyTime) ;
        json.put("bankName", bankName) ;
        json.put("accountNumber", accountNumber) ;
        json.put("bankForkName", bankForkName) ;
        json.put("moneyAmount", amount) ;
        return JSONObject.toJSONString(json);
    }


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankForkName() {
        return bankForkName;
    }

    public void setBankForkName(String bankForkName) {
        this.bankForkName = bankForkName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
}