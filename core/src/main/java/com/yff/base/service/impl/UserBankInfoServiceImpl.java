package com.yff.base.service.impl;

import com.yff.base.common.BitStatesUtils;
import com.yff.base.common.UserContext;
import com.yff.base.domain.UserBankInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.mapper.UserBankInfoMapper;
import com.yff.base.service.IUserBankInfoService;
import com.yff.base.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBankInfoServiceImpl implements IUserBankInfoService {

    @Autowired
    private UserBankInfoMapper userBankInfoMapper;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public UserBankInfo getByUserId(Long userId) {
        return this.userBankInfoMapper.selectByUserId(userId);
    }

    @Override
    public void bindBankInfo(UserBankInfo bankInfo) {
        //1.判断当前用户是否绑定银行卡
        UserInfo current = this.userInfoService.getCurrent();
        if (current != null && !current.getIsBindBank()) {
            UserBankInfo bank = new UserBankInfo();
            //这里强制实名认证名称等于开户名称（也就是绑定银行卡必须先进行实名认证）
            bank.setAccountName(current.getRealName());
            bank.setBankName(bankInfo.getBankName());
            bank.setAccountNumber(bankInfo.getAccountNumber());
            bank.setBankForkName(bankInfo.getBankForkName());
            bank.setLoginInfo(UserContext.getCurrent());
            this.userBankInfoMapper.insert(bank);
            //修改用户绑定银行卡的状态码
            current.addState(BitStatesUtils.OP_HAS_BIND_BANK);
            this.userInfoService.update(current);
        }
    }
}
