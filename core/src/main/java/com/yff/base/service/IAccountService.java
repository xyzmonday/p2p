package com.yff.base.service;

import com.yff.base.domain.Account;

public interface IAccountService {

    void addAccount(Account account);
    Account getAccountById(Long accountId);
    void updateAccount(Account account);
}
