package com.yff.base.query;

import lombok.Getter;
import lombok.Setter;


/**
 * 还款计划查询对象
 */

public class PaymentScheduleQueryObject extends AuditQueryObject {

    private Long loginInfoId = -1L;
    private Long bidRequestId;

    public Long getLoginInfoId() {
        return loginInfoId;
    }

    public void setLoginInfoId(Long loginInfoId) {
        this.loginInfoId = loginInfoId;
    }

    public Long getBidRequestId() {
        return bidRequestId;
    }

    public void setBidRequestId(Long bidRequestId) {
        this.bidRequestId = bidRequestId;
    }
}
