package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.entity.TransactionOutboxEvent;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.repository.TransactionOutboxEventRepository;
import com.banking.TransactionService.service.TransactionOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
@Service
@RequiredArgsConstructor
public class TransactionOutboxServiceImpl implements TransactionOutboxService {

    private final TransactionOutboxEventRepository outboxRepository;

    /*
     * MUST run inside TransactionService transaction
     * → guarantee atomicity (transaction + outbox same commit)
     */
    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void publishDebitRequested(Transaction tx) {
        outboxRepository.save(
                TransactionOutboxEvent.debitRequested(tx)
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void publishCreditRequested(Transaction tx) {
        outboxRepository.save(
                TransactionOutboxEvent.creditRequested(tx)
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void publishCompleted(Transaction tx) {
        outboxRepository.save(
                TransactionOutboxEvent.completed(tx)
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void publishFailed(Transaction tx) {
        outboxRepository.save(
                TransactionOutboxEvent.failed(tx)
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void publishRefundRequested(Transaction tx) {
        outboxRepository.save(
                TransactionOutboxEvent.refundRequested(tx)
        );

    }
}