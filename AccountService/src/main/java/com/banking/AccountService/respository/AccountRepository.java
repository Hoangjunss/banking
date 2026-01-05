package com.banking.AccountService.respository;

import com.banking.AccountService.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account,UUID> {
}
