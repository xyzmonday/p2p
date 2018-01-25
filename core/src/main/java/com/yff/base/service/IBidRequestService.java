package com.yff.base.service;

import com.yff.base.domain.BidRequest;
import com.yff.base.domain.RealAuth;
import com.yff.base.query.BidRequestQueryObject;
import com.yff.base.query.RealAuthQueryObject;

import java.math.BigDecimal;
import java.util.List;

public interface IBidRequestService {

    /**
     * 乐观锁的控制
     *
     * @param bidRequest
     */
    void update(BidRequest bidRequest);


    /**
     * 用户是否能够进行借款申请。必须满足
     * 1.实名认证；
     * 2.基本资料认证；
     * 3.视频认证；
     * 4.风控资料认证；
     *
     * @param loginInfoId
     * @return
     */
    boolean canApplyBidRequest(Long loginInfoId);

    /**
     * 进入借款流程
     *
     * @param bidRequest
     */
    void applyBidRequest(BidRequest bidRequest);

    /**
     * 按条件查询需要发标的所有标
     *
     * @param qo
     * @return
     */
    List<BidRequest> queryByPageCondition(BidRequestQueryObject qo);

    /**
     * 发表前审核
     *
     * @param id:需要审核的标的id
     * @param state:审核状态（通过或者拒绝）
     * @param remark:审核备注
     */
    void publishAudit(Long id, int state, String remark);

    /**
     * 获取一个借款标的信心信息
     *
     * @param id:bidRequest的id
     */
    BidRequest getBidRequestById(Long id);

    /**
     * 投资人进行投标
     *
     * @param bidRequestId:标的id
     * @param amount:投资金额
     */
    void bid(Long bidRequestId, BigDecimal amount);

    /**
     * 满标一审
     *
     * @param bidRequestId:借款id
     * @param state:审核状态（通过或者拒绝）
     * @param remark:审核意见
     */
    void fullAudit1(Long bidRequestId, int state, String remark);

    /**
     * 满标二审
     * @param id
     * @param state
     * @param remark
     */
    void fullAudit2(Long id, int state, String remark);

    /**
     * 还款
     * @param id:还款计划id
     */
    void returnMoney(Long id);
}
