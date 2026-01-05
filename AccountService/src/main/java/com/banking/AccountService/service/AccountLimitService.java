package com.banking.AccountService.service;

import com.banking.AccountService.entity.Account;

import java.util.UUID;

public interface AccountLimitService {

    void setDefaultLimits(Account account);
}
