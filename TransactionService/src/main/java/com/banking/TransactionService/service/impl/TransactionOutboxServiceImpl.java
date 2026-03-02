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
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.MANDATORY)// chạy chung transaction với TransactionService
    @Override
    public void publishTransactionCompleted(Transaction transaction) {

        outboxRepository.save(
                TransactionOutboxEvent.transactionCompleted(transaction)
        );
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize outbox payload", e);
        }
    }
}