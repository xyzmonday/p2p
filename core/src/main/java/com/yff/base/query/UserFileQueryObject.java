package com.yff.base.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


public class UserFileQueryObject extends AuditQueryObject {

    private Long applierId;

    public Long getApplierId() {
        return applierId;
    }

    public void setApplierId(Long applierId) {
        this.applierId = applierId;
    }


}
