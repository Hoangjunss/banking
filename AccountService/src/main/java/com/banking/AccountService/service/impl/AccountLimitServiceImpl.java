package com.banking.AccountService.service.impl;

import com.banking.AccountService.entity.Account;
import com.banking.AccountService.respository.AccountLimitRepository;
import com.banking.AccountService.service.AccountLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AccountLimitServiceImpl implements AccountLimitService {
    private final AccountLimitRepository limitRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void setDefaultLimits(Account account) {
        // Logic tạo hạn mức mặc định (Ngày/Giao dịch)
    }
}
