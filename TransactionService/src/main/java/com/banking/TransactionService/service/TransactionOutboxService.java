package com.banking.TransactionService.service;

import com.banking.TransactionService.entity.Transaction;

public interface TransactionOutboxService {

    void publishDebitRequested(Transaction tx);
    void publishCreditRequested(Transaction tx);
    void publishCompleted(Transaction tx);
    void publishFailed(Transaction tx);
}
