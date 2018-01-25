package com.yff.base.service;

import com.yff.base.domain.RechargeOffline;
import com.yff.base.query.RechargeOfflineQueryObject;

import java.util.List;

public interface IRechargeOfflineService {
    /**
     * 提交线下充值申请
     * @param rechargeOffline
     */
    void apply(RechargeOffline rechargeOffline);


    List<RechargeOffline> queryByPageCondition(RechargeOfflineQueryObject qo);

    /**
     * 充值审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
