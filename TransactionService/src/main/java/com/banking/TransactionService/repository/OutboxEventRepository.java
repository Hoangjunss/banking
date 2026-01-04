package com.banking.TransactionService.repository;

import com.banking.TransactionService.entity.OutboxEvent;
import com.banking.TransactionService.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatus(OutboxStatus status);
}