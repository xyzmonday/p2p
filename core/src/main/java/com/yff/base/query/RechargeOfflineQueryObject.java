package com.yff.base.query;

import com.yff.base.domain.BaseAuditDomain;
import org.springframework.util.StringUtils;

public class RechargeOfflineQueryObject extends AuditQueryObject {

    /**
     * 只能查询自己的充值明细
     */
    private Long applierId;
    //  银行账户信息对应的id
    private Long bankInfoId = -1L;
    //交易流水账号
    private String tradeCode;


    public Long getApplierId() {
        return applierId;
    }

    public void setApplierId(Long applierId) {
        this.applierId = applierId;
    }

    public Long getBankInfoId() {
        return bankInfoId;
    }

    public void setBankInfoId(Long bankInfoId) {
        this.bankInfoId = bankInfoId;
    }

    public String getTradeCode() {
        return StringUtils.hasLength(tradeCode) ? tradeCode : null;
    }


    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }
}
