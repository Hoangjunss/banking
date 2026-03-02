package com.banking.TransactionService.repository;

import com.banking.TransactionService.entity.TransactionOutboxEvent;
import com.banking.TransactionService.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionOutboxEventRepository extends JpaRepository<TransactionOutboxEvent, UUID> {
    List<TransactionOutboxEvent> findByStatusInOrderByCreatedAtAsc(List<OutboxStatus> statuses);
}