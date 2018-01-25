package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.domain.*;
import com.yff.base.mapper.AccountFlowMapper;
import com.yff.base.service.IAccountFlowService;
import com.yff.base.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户流水
 */
@Service
public class AccountFlowServiceImpl implements IAccountFlowService {

    @Autowired
    private AccountFlowMapper accountFlowMapper;

    private AccountFlow createBaseFlow(Account account) {
        AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        //注意账户的可用金额和冻结金额必须现在账户中已经更新过
        accountFlow.setUsableAmount(account.getUsableAmount());
        accountFlow.setFreezedAmount(account.getFreezedAmount());
        accountFlow.setTradeTime(new Date());
        return accountFlow;
    }

    @Override
    public void rechargeFlow(RechargeOffline ro, Account applierAccount) {
        AccountFlow accountFlow = createBaseFlow(applierAccount);
        accountFlow.setAmount(ro.getAmount());
        accountFlow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);
        accountFlow.setNote("线下充值成功;充值金额为" + ro.getAmount());
        accountFlowMapper.insert(accountFlow);
    }

    @Override
    public void bid(Bid bid, Account account) {
        AccountFlow accountFlow = createBaseFlow(account);
        //投标冻结金额
        accountFlow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED);
        accountFlow.setAmount(bid.getAvailableAmount());
        accountFlow.setNote("投标:" + bid.getBidRequestTitle() + "的借款;冻结账户金额" + bid.getAvailableAmount());
        this.accountFlowMapper.insert(accountFlow);
    }

    @Override
    public void returnMoney(Bid bid, Account account, int state) {
        AccountFlow accountFlow = createBaseFlow(account);
        accountFlow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
        accountFlow.setAmount(bid.getAvailableAmount());
        if (1 == state) {
            accountFlow.setNote("投标:" + bid.getBidRequestTitle() + "的借款;满标一审拒绝退款" + bid.getAvailableAmount());
        } else {
            accountFlow.setNote("投标:" + bid.getBidRequestTitle() + "的借款;满标二审拒绝退款" + bid.getAvailableAmount());
        }
        this.accountFlowMapper.insert(accountFlow);
    }

    /**
     * 借款成功，收款流水
     *
     * @param bidRequest
     * @param account
     */
    @Override
    public void borrowSuccess(BidRequest bidRequest, Account account) {
        AccountFlow accountFlow = createBaseFlow(account);
        accountFlow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL);
        accountFlow.setAmount(bidRequest.getBidRequestAmount());
        accountFlow.setNote("借款" + bidRequest.getTitle() + "成功;收到借款金额" + bidRequest.getBidRequestAmount());
        this.accountFlowMapper.insert(accountFlow);
    }

    /**
     * 借款成功，生成手续费流水
     *
     * @param borrowChargeFee
     * @param account
     * @param bidRequest
     */
    @Override
    public void borrowChargeFee(BigDecimal borrowChargeFee, Account account, BidRequest bidRequest) {
        AccountFlow accountFlow = createBaseFlow(account);
        accountFlow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL);
        accountFlow.setAmount(bidRequest.getBidRequestAmount());
        accountFlow.setNote("借款" + bidRequest.getTitle() + "成功,支付借款手续费金额:" + borrowChargeFee);
        this.accountFlowMapper.insert(accountFlow);
    }

    /**
     * 投标成功
     *
     * @param bid
     * @param account
     */
    @Override
    public void bidSuccess(Bid bid, Account account) {
        AccountFlow flow = createBaseFlow(account);
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
        flow.setAmount(bid.getAvailableAmount());
        flow.setNote("投标" + bid.getBidRequestTitle() + "成功,取消投标冻结金额:" + bid.getAvailableAmount());
        this.accountFlowMapper.insert(flow);
    }

    /**
     * 提现申请流水
     *
     * @param moneyWithDraw
     * @param account
     */
    @Override
    public void moneyWithDrawApply(MoneyWithDraw moneyWithDraw, Account account) {
        AccountFlow flow = createBaseFlow(account);
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED);
        flow.setAmount(moneyWithDraw.getAmount());
        flow.setNote("提现申请,冻结金额:" + moneyWithDraw.getAmount());
    }

    /**
     * 提现审核通过
     *
     * @param m
     * @param account
     */
    @Override
    public void moneyWithdrawSuccess(MoneyWithDraw m, Account account) {
        AccountFlow flow = createBaseFlow(account);
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW);
        flow.setAmount(m.getAmount().subtract(m.getCharge()));
        flow.setNote("提现成功,取消冻结金额:" + flow.getAmount());
        this.accountFlowMapper.insert(flow);
    }

    /**
     * 提现审核成功手续费
     *
     * @param m
     * @param account
     */
    @Override
    public void moneyWithdrawCharge(MoneyWithDraw m, Account account) {
        AccountFlow flow = createBaseFlow(account);
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE);
        flow.setAmount(m.getCharge());
        flow.setNote("提现成功,支付提现手续费:" + m.getCharge());
        this.accountFlowMapper.insert(flow);
    }

    /**
     * 提现审核拒绝
     *
     * @param m
     * @param account
     */
    @Override
    public void cancelWithdraw(MoneyWithDraw m, Account account) {
        AccountFlow flow = createBaseFlow(account);
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED);
        flow.setAmount(m.getAmount());
        flow.setNote(m.getApplier().getUsername() + "提现" + m.getAmount() + "失败,取消冻结金额" + m.getAmount());
        this.accountFlowMapper.insert(flow);
    }


    /**
     * 生成成功还款的流水
     */
    @Override
    public void returnMoney(Account account, PaymentSchedule ps) {
        AccountFlow flow = createBaseFlow(account) ;
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY);
        flow.setAmount(ps.getTotalAmount());
        flow.setNote("借款" +ps.getBidRequestTitle()+"第"+ps.getMonthIndex()+"期成功还款!");
        this.accountFlowMapper.insert(flow);
    }

    /**
     * 生成收款人收款流水
     */
    @Override
    public void receiveMoney(Account account, PaymentScheduleDetail psd) {
        AccountFlow flow = createBaseFlow(account) ;
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY);
        flow.setAmount(psd.getTotalAmount());
        flow.setNote("借款第"+psd.getMonthIndex()+"期成功收款!");
        this.accountFlowMapper.insert(flow);
    }

    /**
     * 投资人收款成功,支付利息管理费
     */
    @Override
    public void interestChargeFee(Account account,BigDecimal interestChargeFee, PaymentScheduleDetail psd) {
        AccountFlow flow = createBaseFlow(account) ;
        flow.setAccountType((byte) BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE);
        flow.setAmount(interestChargeFee);
        flow.setNote("借款第"+psd.getMonthIndex()+"期成功收款!支付利息管理费"+interestChargeFee);
        this.accountFlowMapper.insert(flow);
    }
}
