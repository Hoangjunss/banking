package com.banking.TransactionService.service;

import com.banking.TransactionService.entity.Transaction;

public interface TransactionAuditService {
    void logCompleted(Transaction transaction);
}
