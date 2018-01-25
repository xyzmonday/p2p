package com.yff.base.service;

import com.yff.base.domain.BidRequest;
import com.yff.base.domain.MoneyWithDraw;
import com.yff.base.domain.PaymentScheduleDetail;
import com.yff.base.domain.SystemAccount;

import java.math.BigDecimal; /**
 * 系统账户
 */
public interface ISystemAccountService {

    void update(SystemAccount systemAccount);

    /**
     * 创建平台的账户
     */
    void initSystemAccount();

    /**
     * 借款成功，平台收取借款手续费
     * @param borrowChargeFee
     * @param bidRequest
     */
    void chargeBorrowFee(BigDecimal borrowChargeFee, BidRequest bidRequest);

    /**
     * 系统账户收到提现手续费,生成对应流水
     * @param m
     */
    void moneyWithdrawCharge(MoneyWithDraw m);

    /**
     * 收取利息管理费
     * @param interestChargeFee
     * @param psd
     */
    void chargeInterest(BigDecimal interestChargeFee, PaymentScheduleDetail psd);
}
