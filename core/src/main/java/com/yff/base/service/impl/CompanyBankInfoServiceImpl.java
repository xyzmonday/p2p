package com.yff.base.service.impl;

import com.yff.base.domain.CompanyBankInfo;
import com.yff.base.mapper.CompanyBankInfoMapper;
import com.yff.base.query.PlatformBankInfoQueryObject;
import com.yff.base.service.ICompanyBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台银行账户
 */
@Service
public class CompanyBankInfoServiceImpl implements ICompanyBankInfoService {

    @Autowired
    private CompanyBankInfoMapper companyBankInfoMapper;

    @Override
    public List<CompanyBankInfo> queryByPageCondition(PlatformBankInfoQueryObject qo) {
        return companyBankInfoMapper.queryByPageCondition(qo);
    }

    @Override
    public void saveOrUpdate(CompanyBankInfo companyBankInfo) {
        if (companyBankInfo.getId() != null) {
            this.companyBankInfoMapper.updateByPrimaryKey(companyBankInfo);
        } else {
            this.companyBankInfoMapper.insert(companyBankInfo);
        }
    }

    @Override
    public int getRowCount(Long id) {
        return companyBankInfoMapper.queryRowCount(id);
    }
}
