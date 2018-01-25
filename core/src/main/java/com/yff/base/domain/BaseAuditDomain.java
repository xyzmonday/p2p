package com.yff.base.domain;

import com.yff.base.common.BidConst;

import java.util.Date;

public abstract class BaseAuditDomain extends BaseDomain {
    //申请的基本属性
    //前台申请人
    protected LoginInfo applier;
    //申请时间
    protected Date applyTime;
    //审核备注
    protected String remark;
    //审核状态（审核通过或者拒绝）
    protected Byte state;
    //审核时间
    protected Date auditTime;
    //后台审核人
    protected LoginInfo auditor;

    public String getStateDisplay() {
        switch (state) {
            case BidConst.AUDIT_STATE_NORMAL:
                return "审核中";
            case BidConst.AUDIT_STATE_AUTH:
                return "审核通过";
            case BidConst.AUDIT_STATE_REJECT:
                return "审核拒绝";
            default:
                return "";
        }
    }

    public LoginInfo getApplier() {
        return applier;
    }

    public void setApplier(LoginInfo applier) {
        this.applier = applier;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public LoginInfo getAuditor() {
        return auditor;
    }

    public void setAuditor(LoginInfo auditor) {
        this.auditor = auditor;
    }
}
