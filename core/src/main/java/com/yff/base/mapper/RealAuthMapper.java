package com.yff.base.mapper;

import com.yff.base.domain.RealAuth;
import com.yff.base.query.RealAuthQueryObject;
import com.yff.base.query.UserFileQueryObject;

import java.util.List;

public interface RealAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RealAuth record);

    int insertSelective(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RealAuth record);

    int updateByPrimaryKey(RealAuth record);

    List<RealAuth> queryByPageCondition(RealAuthQueryObject qo);
}