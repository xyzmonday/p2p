package com.yff.base.mapper;

import com.yff.base.domain.RealAuth;
import com.yff.base.domain.VedioAuth;
import com.yff.base.query.VedioAuthQueryObject;

import java.util.List;

public interface VedioAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VedioAuth record);

    int insertSelective(VedioAuth record);

    VedioAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VedioAuth record);

    int updateByPrimaryKey(VedioAuth record);


    List<VedioAuth> queryByPageCondition(VedioAuthQueryObject qo);
}