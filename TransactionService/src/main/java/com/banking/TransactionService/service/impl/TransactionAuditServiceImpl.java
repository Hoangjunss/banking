package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.entity.AuditAction;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionAuditLog;
import com.banking.TransactionService.repository.TransactionAuditLogRepository;
import com.banking.TransactionService.service.TransactionAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionAuditServiceImpl implements TransactionAuditService {

    private final TransactionAuditLogRepository repository;

    @Override
    public void log(Transaction tx, AuditAction action, String details) {
        repository.save(TransactionAuditLog.of(tx, action, details));
    }

    /* convenience methods */

    @Override
    public void logCreated(Transaction tx) {
        log(tx, AuditAction.CREATED, "Transaction created");
    }

    @Override
    public void logProcessing(Transaction tx) {
        log(tx, AuditAction.PROCESSING, "Saga started");
    }

    @Override
    public void logCompleted(Transaction tx) {
        log(tx, AuditAction.COMPLETED, "Transaction completed successfully");
    }

    @Override
    public void logFailed(Transaction tx, String reason) {
        log(tx, AuditAction.FAILED, reason);
    }

    @Override
    public void logReversed(Transaction tx) {
        log(tx, AuditAction.REVERSED, "Compensation executed");
    }
}