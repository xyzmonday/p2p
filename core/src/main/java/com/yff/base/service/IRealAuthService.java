package com.yff.base.service;

import com.yff.base.domain.RealAuth;
import com.yff.base.query.RealAuthQueryObject;

import java.util.List;

public interface IRealAuthService {

    List<RealAuth> getAppliedRealAuthList(RealAuthQueryObject qo);

    /**
     * 通过id获取到实名认证实体
     * @param id
     * @return
     */
    RealAuth getById(Long id);

    /**
     * 用户提交实名认证申请
     * @param realAuth
     */
    void apply(RealAuth realAuth);

    /**
     * 后台审核实名申请
     * @param id:本次申请的id
     * @param remark:审核人备注
     * @param state:拒绝或者审核通过
     */
    void audit(Long id,String remark,int state);
}
