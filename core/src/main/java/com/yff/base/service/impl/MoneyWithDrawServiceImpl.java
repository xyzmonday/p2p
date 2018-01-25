package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.BitStatesUtils;
import com.yff.base.common.UserContext;
import com.yff.base.domain.Account;
import com.yff.base.domain.MoneyWithDraw;
import com.yff.base.domain.UserBankInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.mapper.MoneyWithDrawMapper;
import com.yff.base.query.MoneyWithdrawQueryObject;
import com.yff.base.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 提现
 */
@Service
public class MoneyWithDrawServiceImpl implements IMoneyWithDrawService {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IUserBankInfoService userBankInfoService;
    @Autowired
    private MoneyWithDrawMapper moneyWithDrawMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ISystemAccountService systemAccountService;


    @Override
    public void apply(BigDecimal moneyAmount) {
        //判断用户是否有一个提现申请，因为用户只能每一次申请一个
        UserInfo current = this.userInfoService.getCurrent();
        Account account = this.accountService.getAccountById(current.getId());
        //提现金额必须小于等于用户可用金额，提现金额大于等于系统最小提现金额
        if (current != null && !current.getHasWithdrawInProcess()
                && moneyAmount.compareTo(account.getUsableAmount()) <= 0
                && moneyAmount.compareTo(BidConst.MIN_WITHDRAW_AMOUNT) >= 0) {
            //没有提现申请
            //创建一个申请对象
            UserBankInfo userBankInfo = this.userBankInfoService.getByUserId(current.getId());
            MoneyWithDraw moneyWithDraw = new MoneyWithDraw();
            moneyWithDraw.setAccountName(userBankInfo.getAccountName());
            moneyWithDraw.setAccountNumber(userBankInfo.getAccountNumber());
            moneyWithDraw.setBankName(userBankInfo.getBankName());
            moneyWithDraw.setBankForkName(userBankInfo.getBankForkName());
            moneyWithDraw.setAmount(moneyAmount);
            moneyWithDraw.setApplier(UserContext.getCurrent());
            moneyWithDraw.setApplyTime(new Date());
            //手续费
            moneyWithDraw.setCharge(BidConst.MONEY_WITHDRAW_CHARGEFEE);
            moneyWithDraw.setState((byte) BidConst.AUDIT_STATE_NORMAL);
            this.moneyWithDrawMapper.insert(moneyWithDraw);

            //用户账户
            account.setUsableAmount(account.getUsableAmount().subtract(moneyAmount));
            account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));
            this.accountFlowService.moneyWithDrawApply(moneyWithDraw, account);
            this.accountService.updateAccount(account);

            //用户增加状态码
            current.addState(BitStatesUtils.OP_HAS_WITHDRAW_PROCESS);
            this.userInfoService.update(current);
        }
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //得到提现申请, 判断状态
        MoneyWithDraw m = this.moneyWithDrawMapper.selectByPrimaryKey(id);
        if (m != null && m.getState() == BidConst.AUDIT_STATE_NORMAL) {
            //设置相关属性
            m.setAuditor(UserContext.getCurrent());
            m.setAuditTime(new Date());
            m.setRemark(remark);
            m.setState((byte) state);
            UserInfo userinfo = this.userInfoService.getUserInfo(m.getApplier().getId());
            Account account = this.accountService.getAccountById(userinfo.getId());
            if (state == BidConst.AUDIT_STATE_AUTH) {
                // 审核通过
                // 减少冻结金额,生成一条提现流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(m.getAmount().subtract(m.getCharge())));
                this.accountFlowService.moneyWithdrawSuccess(m, account);
                // 减少冻结金额,生成支付提现手续费流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(m.getCharge()));
                this.accountFlowService.moneyWithdrawCharge(m, account);
                //系统账户收到提现手续费,生成对应流水
                this.systemAccountService.moneyWithdrawCharge(m);
            } else {
                // 审核失败
                // 减少冻结金额,增加可用余额, 生成提现取消流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(m.getAmount()));
                account.setUsableAmount(account.getUsableAmount().add(m.getAmount()));
                this.accountFlowService.cancelWithdraw(m, account);
            }
            // 取消状态码
            userinfo.removeState(BitStatesUtils.OP_HAS_WITHDRAW_PROCESS);
            this.userInfoService.update(userinfo);
            this.accountService.updateAccount(account);
            this.moneyWithDrawMapper.updateByPrimaryKey(m);
        }
    }

    @Override
    public List<MoneyWithDraw> queryByPageCondition(MoneyWithdrawQueryObject qo) {
        return moneyWithDrawMapper.queryByPageCondition(qo);
    }
}
