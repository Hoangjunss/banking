package com.banking.TransactionService.service;

import com.banking.TransactionService.entity.Transaction;

public interface OutboxService {
    void publishTransactionCompleted(Transaction transaction);
}
