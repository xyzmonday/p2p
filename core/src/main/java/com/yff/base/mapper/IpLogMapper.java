package com.yff.base.mapper;

import com.yff.base.domain.IpLog;
import com.yff.base.query.IpLogQueryObject;

import java.util.List;

public interface IpLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IpLog record);

    int insertSelective(IpLog record);

    IpLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IpLog record);

    int updateByPrimaryKey(IpLog record);

    List<IpLog> queryByPageCondition(IpLogQueryObject qo);
}