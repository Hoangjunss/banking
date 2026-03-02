package com.banking.TransactionService.messaging;

import com.banking.TransactionService.dto.internal.SagaEvent;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionStatus;
import com.banking.TransactionService.repository.TransactionRepository;
import com.banking.TransactionService.service.TransactionOutboxService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionSagaListener {

    private final TransactionRepository transactionRepository;
    private final TransactionOutboxService outboxService;
    private final ObjectMapper objectMapper;


    /*
     =========================================================
     STEP 1 RESULT → debit completed
     =========================================================
     AccountService publish: DEBIT_COMPLETED
     → now ask CREDIT
     */
    @KafkaListener(topics = "balance-events", groupId = "transaction-service")
    @Transactional
    public void handleAccountEvents(String message) throws Exception {

        SagaEvent event = objectMapper.readValue(message, SagaEvent.class);

        Transaction tx = transactionRepository
                .findById(UUID.fromString(event.getTransactionId()))
                .orElseThrow();

        switch (event.getType()) {

            case "DEBIT_COMPLETED" -> handleDebitCompleted(tx);

            case "CREDIT_COMPLETED" -> handleCreditCompleted(tx);

            case "DEBIT_FAILED", "CREDIT_FAILED" -> handleFailed(tx);
        }
    }

    /*
     ==============================
     debit ok → request credit
     ==============================
     */
    private void handleDebitCompleted(Transaction tx) {
        log.info("Debit success → request credit: {}", tx.getId());

        outboxService.publishCreditRequested(tx);
    }

    /*
     ==============================
     credit ok → SUCCESS 🎉
     ==============================
     */
    private void handleCreditCompleted(Transaction tx) {
        log.info("Transfer completed: {}", tx.getId());

        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setCompletedAt(Instant.now());

        transactionRepository.save(tx);

        outboxService.publishCompleted(tx);
    }

    /*
     ==============================
     any step fail → FAILED
     ==============================
     */
    private void handleFailed(Transaction tx) {
        log.info("Transfer failed: {}", tx.getId());

        tx.setStatus(TransactionStatus.FAILED);

        transactionRepository.save(tx);

        outboxService.publishFailed(tx);
    }
}