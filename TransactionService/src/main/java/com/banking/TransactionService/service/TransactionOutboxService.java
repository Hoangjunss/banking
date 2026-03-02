package com.banking.TransactionService.service;

import com.banking.TransactionService.entity.Transaction;

public interface TransactionOutboxService {
    void publishTransactionCompleted(Transaction transaction);
}
