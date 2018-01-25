package com.yff.base.mapper;

import com.yff.base.domain.PaymentSchedule;
import com.yff.base.query.PaymentScheduleQueryObject;

import java.util.List;

public interface PaymentScheduleMapper {

    int insert(PaymentSchedule record);

    int insertSelective(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentSchedule record);

    int updateByPrimaryKey(PaymentSchedule record);

    List<PaymentSchedule> queryByPageCondition(PaymentScheduleQueryObject qo);
}