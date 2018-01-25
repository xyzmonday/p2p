package com.yff.base.mapper;

import com.yff.base.domain.RechargeOffline;
import com.yff.base.query.RechargeOfflineQueryObject;

import java.util.List;

public interface RechargeOfflineMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RechargeOffline record);

    int insertSelective(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RechargeOffline record);

    int updateByPrimaryKey(RechargeOffline record);

    List<RechargeOffline> queryByPageCondition(RechargeOfflineQueryObject qo);
}