package com.yff.base.domain;

import com.alibaba.fastjson.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.HashMap;
import java.util.Map;

public class CompanyBankInfo {
    private Long id;
    //银行名
    private String bankName;
    //开户人
    private String accountName;
    //账户
    private String bankNumber;
    //银行支行
    private String bankForkName;

    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("bankName", bankName);
        json.put("accountName", accountName);
        json.put("bankNumber", bankNumber);
        json.put("bankForkName", bankForkName);
        return JSONObject.toJSONString(json);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber == null ? null : bankNumber.trim();
    }

    public String getBankForkName() {
        return bankForkName;
    }

    public void setBankForkName(String bankForkName) {
        this.bankForkName = bankForkName == null ? null : bankForkName.trim();
    }
}