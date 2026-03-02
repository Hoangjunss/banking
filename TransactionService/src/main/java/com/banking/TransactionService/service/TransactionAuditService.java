package com.banking.TransactionService.service;

import com.banking.TransactionService.entity.AuditAction;
import com.banking.TransactionService.entity.Transaction;

public interface TransactionAuditService {
    void log(Transaction tx, AuditAction action, String details);
    void logCreated(Transaction tx);
    void logProcessing(Transaction tx);
    void logCompleted(Transaction tx);
    void logFailed(Transaction tx, String reason);
    void logReversed(Transaction tx);
}
