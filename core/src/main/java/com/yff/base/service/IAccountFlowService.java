package com.yff.base.service;

import com.yff.base.domain.*;

import java.math.BigDecimal;

public interface IAccountFlowService {


    /**
     * 生成充值账户流水
     *
     * @param ro
     * @param applierAccount
     */
    void rechargeFlow(RechargeOffline ro, Account applierAccount);

    /**
     * 生成投标流水
     *
     * @param bid
     * @param currentAccount
     */
    void bid(Bid bid, Account currentAccount);

    /**
     * 满标审核退款流水
     *
     * @param bid
     * @param account
     * @param state:满标一审（1）或者满标二审（2）
     */
    void returnMoney(Bid bid, Account account, int state);

    /**
     * 满标二审通过，生成收款流水。
     *
     * @param bidRequest
     * @param borrowAccount
     */
    void borrowSuccess(BidRequest bidRequest, Account borrowAccount);

    /**
     * 借款成功后，手续费流水
     *
     * @param borrowChargeFee
     * @param borrowAccount
     * @param bidRequest
     */
    void borrowChargeFee(BigDecimal borrowChargeFee, Account borrowAccount, BidRequest bidRequest);

    /**
     * 投标成功后流水
     *
     * @param bid
     * @param bidAccount
     */
    void bidSuccess(Bid bid, Account bidAccount);

    /**
     * 生成用户提现申请流水
     *
     * @param moneyWithDraw
     * @param account
     */
    void moneyWithDrawApply(MoneyWithDraw moneyWithDraw, Account account);

    /**
     * 提现审核成功流水
     *
     * @param m
     * @param account
     */
    void moneyWithdrawSuccess(MoneyWithDraw m, Account account);

    /**
     * 提现审核通过，手续费流水
     *
     * @param m
     * @param account
     */
    void moneyWithdrawCharge(MoneyWithDraw m, Account account);

    /**
     * 提现审核失败，减少冻结金额,增加可用余额生成对应的流水
     *
     * @param m
     * @param account
     */
    void cancelWithdraw(MoneyWithDraw m, Account account);

    /**
     * 投资人收款成功,支付利息管理费
     * @param bidAccount
     * @param interestChargeFee
     * @param psd
     */
    void interestChargeFee(Account bidAccount, BigDecimal interestChargeFee, PaymentScheduleDetail psd);

    /**
     * 生成收款收到的本息流水
     * @param bidAccount
     * @param psd
     */
    void receiveMoney(Account bidAccount, PaymentScheduleDetail psd);

    /**
     * 生成成功还款流水
     * @param ps
     */
    void returnMoney(Account account, PaymentSchedule ps);
}
