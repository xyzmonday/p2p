package com.yff.base.domain;

import org.springframework.util.StringUtils;

public class UserBankInfo {
    private Long id;
    //银行名
    private String bankName;
    //开户名
    private String accountName;
    //银行账号
    private String accountNumber;
    //银行支行名称
    private String bankForkName;

    private LoginInfo loginInfo;


    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     *
     *            真实名字
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.accountName)) {
            int len = accountName.length();
            String replace = "";
            replace += accountName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return accountName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @return
     */
    public String getAnonymousAccountNumber() {
        if (StringUtils.hasLength(accountNumber)) {
            int len = accountNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += accountNumber.charAt(i);
                }
            }
            return replace;
        }
        return accountNumber;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? null : accountNumber.trim();
    }

    public String getBankForkName() {
        return bankForkName;
    }

    public void setBankForkName(String bankForkName) {
        this.bankForkName = bankForkName == null ? null : bankForkName.trim();
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }
}