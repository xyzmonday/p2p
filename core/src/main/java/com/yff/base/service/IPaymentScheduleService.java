package com.yff.base.service;

import com.yff.base.domain.PaymentSchedule;
import com.yff.base.query.PaymentScheduleQueryObject;

import java.util.List;

public interface IPaymentScheduleService {

    List<PaymentSchedule> queryByPageCondition(PaymentScheduleQueryObject qo);
}
