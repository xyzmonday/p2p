package com.yff.base.mapper;

import com.yff.base.domain.CompanyBankInfo;
import com.yff.base.query.PlatformBankInfoQueryObject;

import java.util.List;

public interface CompanyBankInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CompanyBankInfo record);

    int insertSelective(CompanyBankInfo record);

    CompanyBankInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CompanyBankInfo record);

    int updateByPrimaryKey(CompanyBankInfo record);

    List<CompanyBankInfo> queryByPageCondition(PlatformBankInfoQueryObject qo);

    int queryRowCount(Long id);
}