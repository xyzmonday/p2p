package com.yff.base.mapper;

import com.yff.base.domain.AccountFlow;

public interface AccountFlowMapper {

    int insert(AccountFlow record);

    int insertSelective(AccountFlow record);

    AccountFlow selectByPrimaryKey(Long id);

}