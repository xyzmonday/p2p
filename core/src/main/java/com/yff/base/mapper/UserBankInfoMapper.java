package com.yff.base.mapper;

import com.yff.base.domain.UserBankInfo;

public interface UserBankInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBankInfo record);

    int insertSelective(UserBankInfo record);

    UserBankInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBankInfo record);

    int updateByPrimaryKey(UserBankInfo record);

    UserBankInfo selectByUserId(Long userId);
}