package com.yff.base.service.impl;

import com.yff.base.domain.Account;
import com.yff.base.mapper.AccountMapper;
import com.yff.base.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void addAccount(Account account) {
        accountMapper.insert(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountMapper.selectByPrimaryKey(accountId);
    }

    @Override
    public void updateAccount(Account account) {
        int ret = this.accountMapper.updateByPrimaryKey(account);
        if (ret <= 0) {
            throw new RuntimeException("Account乐观锁失败");
        }
    }
}
