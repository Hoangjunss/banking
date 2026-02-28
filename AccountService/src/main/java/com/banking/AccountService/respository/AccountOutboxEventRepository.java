package com.banking.AccountService.respository;

import com.banking.AccountService.entity.AccountOutboxEvent;
import com.banking.AccountService.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountOutboxEventRepository extends JpaRepository<AccountOutboxEvent,UUID> {
    List<AccountOutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
