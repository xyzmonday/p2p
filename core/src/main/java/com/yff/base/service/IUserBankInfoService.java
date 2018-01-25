package com.yff.base.service;

import com.yff.base.domain.UserBankInfo;

public interface IUserBankInfoService {
    /**
     * 得到当前用户绑定的银行卡信息
     * @param userId
     * @return
     */
    UserBankInfo getByUserId(Long userId);

    /**
     * 绑定银行卡
     * @param bankInfo
     */
    void bindBankInfo(UserBankInfo bankInfo);
}
