package com.banking.TransactionService.repository;

import com.banking.TransactionService.entity.TransactionAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionAuditLogRepository
        extends JpaRepository<TransactionAuditLog, UUID> {

    List<TransactionAuditLog> findByTransactionId(UUID transactionId);
}