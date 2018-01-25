package com.yff.base.service;

import com.yff.base.domain.CompanyBankInfo;
import com.yff.base.query.PlatformBankInfoQueryObject;

import java.util.List;

public interface ICompanyBankInfoService {

    List<CompanyBankInfo> queryByPageCondition(PlatformBankInfoQueryObject qo);

    /**
     * 修改或者保存平台银行账户
     * @param companyBankInfo
     */
    void saveOrUpdate(CompanyBankInfo companyBankInfo);

    /**
     * 获取记录的总行数
     * @return
     */
    int getRowCount(Long id);
}
