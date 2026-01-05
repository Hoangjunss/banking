package com.banking.AccountService.respository;

import com.banking.AccountService.entity.AccountOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountOutboxEventRepository extends JpaRepository<AccountOutboxEvent,UUID> {
}
