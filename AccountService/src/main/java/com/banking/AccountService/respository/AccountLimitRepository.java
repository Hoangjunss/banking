package com.banking.AccountService.respository;

import com.banking.AccountService.entity.AccountLimit;
import com.banking.AccountService.entity.LimitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountLimitRepository extends JpaRepository<AccountLimit,UUID> {
    Optional<AccountLimit> findByAccountIdAndType(UUID uuid, LimitType type);
}
