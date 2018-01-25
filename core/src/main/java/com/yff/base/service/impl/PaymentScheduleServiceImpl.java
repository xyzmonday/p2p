package com.yff.base.service.impl;

import com.yff.base.domain.PaymentSchedule;
import com.yff.base.mapper.PaymentScheduleMapper;
import com.yff.base.query.PaymentScheduleQueryObject;
import com.yff.base.service.IPaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements IPaymentScheduleService{

    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;


    @Override
    public List<PaymentSchedule> queryByPageCondition(PaymentScheduleQueryObject qo) {
        return paymentScheduleMapper.queryByPageCondition(qo);
    }
}
