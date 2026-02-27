package com.banking.AccountService.service.impl;

import com.banking.AccountService.dto.request.SetAccountLimitRequestDTO;
import com.banking.AccountService.entity.Account;
import com.banking.AccountService.entity.AccountLimit;
import com.banking.AccountService.entity.LimitType;
import com.banking.AccountService.respository.AccountLimitRepository;
import com.banking.AccountService.service.AccountLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountLimitServiceImpl implements AccountLimitService {
    private final AccountLimitRepository limitRepository;

    private static final Map<LimitType, BigDecimal> DEFAULT_LIMITS = new EnumMap<>(LimitType.class);

    static {
        DEFAULT_LIMITS.put(LimitType.PER_TRANSACTION, new BigDecimal("5000000"));
        DEFAULT_LIMITS.put(LimitType.DAILY_TRANSFER, new BigDecimal("20000000"));
        DEFAULT_LIMITS.put(LimitType.MONTHLY_WITHDRAW, new BigDecimal("200000000"));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void setDefaultLimits(Account account) {
        for (Map.Entry<LimitType, BigDecimal> entry : DEFAULT_LIMITS.entrySet()) {
            LimitType type = entry.getKey();
            if (limitRepository.findByAccountIdAndType(account.getId(), type).isPresent()) {
                continue;
            }

            AccountLimit limit = new AccountLimit();
            limit.setId(UUID.randomUUID());
            limit.setAccountId(account.getId());
            limit.setType(type);
            limit.setAmount(entry.getValue());
            limitRepository.save(limit);
        }
    }

    @Override
    public void updateLimit(Account account, SetAccountLimitRequestDTO request) {
        AccountLimit limit = limitRepository.findByAccountIdAndType(account.getId(), LimitType.valueOf(request.getType()))
                .orElseGet(() -> {
                    AccountLimit created = new AccountLimit();
                    created.setId(UUID.randomUUID());
                    created.setAccountId(account.getId());
                    created.setType(LimitType.valueOf(request.getType()));
                    return created;
                });

        limit.setAmount(request.getAmount());
        limitRepository.save(limit);
    }


}
