package com.yff.base.mapper;

import com.yff.base.domain.PaymentScheduleDetail;

public interface PaymentScheduleDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentScheduleDetail record);

    int insertSelective(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentScheduleDetail record);

    int updateByPrimaryKey(PaymentScheduleDetail record);
}