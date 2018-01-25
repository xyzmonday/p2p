package com.yff.base.mapper;

import com.yff.base.domain.BidRequest;
import com.yff.base.domain.MoneyWithDraw;
import com.yff.base.query.MoneyWithdrawQueryObject;

import java.util.List;

public interface MoneyWithDrawMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MoneyWithDraw record);

    int insertSelective(MoneyWithDraw record);

    MoneyWithDraw selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MoneyWithDraw record);

    int updateByPrimaryKey(MoneyWithDraw record);

    List<MoneyWithDraw> queryByPageCondition(MoneyWithdrawQueryObject qo);
}