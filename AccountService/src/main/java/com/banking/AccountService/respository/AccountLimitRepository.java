package com.banking.AccountService.respository;

import com.banking.AccountService.entity.AccountLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountLimitRepository extends JpaRepository<AccountLimit,UUID> {
}
