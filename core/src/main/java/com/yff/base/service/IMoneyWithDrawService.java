package com.yff.base.service;

import com.yff.base.domain.MoneyWithDraw;
import com.yff.base.query.MoneyWithdrawQueryObject;

import java.math.BigDecimal;
import java.util.List;

public interface IMoneyWithDrawService {

    /**
     * 提现申请
     * @param moneyAmount
     */
    void apply(BigDecimal moneyAmount);

    /**
     * 提现审核
     * @param id
     * @param remark
     * @param state
     */
    void audit(Long id, String remark, int state);

    List<MoneyWithDraw> queryByPageCondition(MoneyWithdrawQueryObject qo);
}
