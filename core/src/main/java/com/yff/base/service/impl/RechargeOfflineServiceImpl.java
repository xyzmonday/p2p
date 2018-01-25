package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.UserContext;
import com.yff.base.domain.Account;
import com.yff.base.domain.AccountFlow;
import com.yff.base.domain.RechargeOffline;
import com.yff.base.mapper.CompanyBankInfoMapper;
import com.yff.base.mapper.RechargeOfflineMapper;
import com.yff.base.query.RechargeOfflineQueryObject;
import com.yff.base.service.IAccountFlowService;
import com.yff.base.service.IAccountService;
import com.yff.base.service.ICompanyBankInfoService;
import com.yff.base.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 线下充值。线下充值的流程是用户到p2p平台自动的银行账户，生成交易流水号，
 * 填写线下充值的申请，后台人员审核线下充值是否到账。
 */
@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {


    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private ICompanyBankInfoService companyBankInfoService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IAccountService accountService;

    @Override
    public void apply(RechargeOffline rechargeOffline) {
        //检查平台账户是否存在
        if (rechargeOffline != null && rechargeOffline.getBankInfo().getId() != null) {
            int rowCount = companyBankInfoService.getRowCount(rechargeOffline.getBankInfo().getId());
            if (rowCount <= 0) {
                throw new RuntimeException("您充值的账户不存在");
            }
        }
        rechargeOffline.setApplier(UserContext.getCurrent());
        rechargeOffline.setApplyTime(new Date());
        rechargeOffline.setState((byte) BidConst.AUDIT_STATE_NORMAL);
        this.rechargeOfflineMapper.insertSelective(rechargeOffline);

    }

    @Override
    public List<RechargeOffline> queryByPageCondition(RechargeOfflineQueryObject qo) {
        return rechargeOfflineMapper.queryByPageCondition(qo);
    }

    /**
     * 线下充值审核
     * @param id
     * @param state
     * @param remark
     */
    @Override
    public void audit(Long id, int state, String remark) {
        //1.查询审核明细
        RechargeOffline ro = this.rechargeOfflineMapper.selectByPrimaryKey(id);
        //还没有审核
        if(ro != null && ro.getState() == BidConst.AUDIT_STATE_NORMAL) {
             ro.setAuditor(UserContext.getCurrent());
             ro.setAuditTime(new Date());
             ro.setRemark(remark);
             ro.setState((byte) state);
             if(state == BidConst.AUDIT_STATE_AUTH) {
                 //3.审核通过
                 //3.1 得到申请人的账户对象
                 Account applierAccount = this.accountService.getAccountById(ro.getApplier().getId());
                 //3.2 增加账户的可用余额
                 applierAccount.setUsableAmount(applierAccount.getUsableAmount().add(ro.getAmount()));
                 //3.3 生成一条充值流水
                 this.accountFlowService.rechargeFlow(ro,applierAccount);
                 //3.4更新账户信息
                 this.accountService.updateAccount(applierAccount);
             }
             this.rechargeOfflineMapper.updateByPrimaryKey(ro);
        }
    }
}
