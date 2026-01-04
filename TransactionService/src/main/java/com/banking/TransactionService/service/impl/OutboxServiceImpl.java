package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.entity.OutboxEvent;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.repository.OutboxEventRepository;
import com.banking.TransactionService.service.OutboxService;

public class OutboxServiceImpl implements OutboxService {
    private final OutboxEventRepository outboxRepository;


    public OutboxServiceImpl(OutboxEventRepository outboxRepository){
        this.outboxRepository=outboxRepository;
    }
    @Override
    public void publishTransactionCompleted(Transaction transaction) {
        outboxRepository.save(
                OutboxEvent.transactionCompleted(transaction)
        );
    }
}
