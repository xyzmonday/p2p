package com.yff.base.mapper;

import com.yff.base.domain.SystemAccount;

public interface SystemAccountMapper {

    int insert(SystemAccount record);

    int insertSelective(SystemAccount record);

    SystemAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemAccount record);

    int updateByPrimaryKey(SystemAccount record);

    SystemAccount selectCurrent();
}