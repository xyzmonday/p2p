package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.domain.*;
import com.yff.base.mapper.SystemAccountFlowMapper;
import com.yff.base.mapper.SystemAccountMapper;
import com.yff.base.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SystemAccountServiceImpl implements ISystemAccountService {

    @Autowired
    private SystemAccountMapper systemAccountMapper;
    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;

    public void update(SystemAccount systemAccount) {
        int ret = this.systemAccountMapper.updateByPrimaryKey(systemAccount);
        if (ret <= 0) {
            throw new RuntimeException("SystemAccount乐观锁失败");
        }
    }

    @Override
    public void initSystemAccount() {
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        if (current == null) {
            current = new SystemAccount();
            current.setCreateDate(new Date());
            this.systemAccountMapper.insert(current);
        }
    }


    @Override
    public void chargeBorrowFee(BigDecimal borrowChargeFee, BidRequest bidRequest) {
        //1.获取当前系统的账户
        SystemAccount currentAccount = this.systemAccountMapper.selectCurrent();
        //修改系统账户余额
        currentAccount.setUsableAmount(currentAccount.getUsableAmount().add(borrowChargeFee));
        //2.生成一条系统账户流水
        SystemAccountFlow systemAccountFlow = new SystemAccountFlow();
        systemAccountFlow.setAccountId(currentAccount.getId());
        systemAccountFlow.setAccountType((byte) BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE);
        systemAccountFlow.setAmount(borrowChargeFee);
        systemAccountFlow.setCreatedDate(new Date());
        systemAccountFlow.setUsableAmount(currentAccount.getUsableAmount());
        systemAccountFlow.setFreezedAmount(currentAccount.getFreezedAmount());
        systemAccountFlow.setNote("借款" + bidRequest.getTitle() + "成功,收取借款管理费" + borrowChargeFee);
        this.systemAccountFlowMapper.insert(systemAccountFlow);
        this.update(currentAccount);
    }

    /**
     * 系统收取提现手续费
     *
     * @param m
     */
    @Override
    public void moneyWithdrawCharge(MoneyWithDraw m) {
        //获取当前的系统账户
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        //修改系统账户余额
        current.setUsableAmount(current.getUsableAmount().add(m.getCharge()));
        //生成一条系统账户流水
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setAccountType((byte) BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE);
        flow.setAccountId(current.getId());
        flow.setAmount(m.getCharge());
        flow.setCreatedDate(new Date());
        flow.setUsableAmount(current.getUsableAmount());
        flow.setFreezedAmount(current.getFreezedAmount());
        flow.setNote(m.getApplier().getUsername() + "提现" + m.getAmount() + "成功,收取提现手续费" + m.getCharge());
        this.systemAccountFlowMapper.insert(flow);
        this.update(current);
    }

    @Override
    public void chargeInterest(BigDecimal interestChargeFee, PaymentScheduleDetail psd) {
        //获取当前的系统账户
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        //修改系统账户余额
        current.setUsableAmount(current.getUsableAmount().add(interestChargeFee));
        //生成一条系统账户流水
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setAccountType((byte) BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE);
        flow.setAccountId(current.getId());
        flow.setAmount(interestChargeFee);
        flow.setCreatedDate(new Date());
        flow.setUsableAmount(current.getUsableAmount());
        flow.setFreezedAmount(current.getFreezedAmount());
        flow.setNote(psd.getToLoginInfoId() + "收款" + psd.getTotalAmount() + "成功,收取利息管理费" + interestChargeFee);
        this.systemAccountFlowMapper.insert(flow);
        this.update(current);
    }


}
