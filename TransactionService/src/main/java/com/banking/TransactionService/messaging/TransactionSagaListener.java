package com.banking.TransactionService.messaging;

import com.banking.TransactionService.dto.internal.SagaEvent;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionStatus;
import com.banking.TransactionService.repository.TransactionRepository;
import com.banking.TransactionService.service.TransactionOutboxService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    public void handleBalanceEvents(ConsumerRecord<String, String> record) {
        try {
            SagaEvent event = objectMapper.readValue(record.value(), SagaEvent.class);

            Transaction tx = transactionRepository
                    .findById(UUID.fromString(event.getTransactionId()))
                    .orElseThrow(() -> new RuntimeException(
                            "Transaction not found: " + event.getTransactionId()));

            switch (event.getType()) {
                case "DEBIT_COMPLETED"  -> handleDebitCompleted(tx);
                case "CREDIT_COMPLETED" -> handleCreditCompleted(tx);
                case "DEBIT_FAILED"     -> handleDebitFailed(tx, event.getReason());
                case "CREDIT_FAILED"    -> handleCreditFailed(tx, event.getReason());
                case "REFUND_COMPLETED" -> handleRefundCompleted(tx);
                default -> log.warn("Unknown saga event: {}", event.getType());
            }

        } catch (Exception e) {
            log.error("Failed to process saga event: {}", record.value(), e);
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

    // Debit fail → không có gì để hoàn, mark failed luôn
    private void handleDebitFailed(Transaction tx, String reason) {
        log.warn(" Debit failed ({}): {}", reason, tx.getId());
        tx.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(tx);
        outboxService.publishFailed(tx);
    }

    private void handleCreditFailed(Transaction tx, String reason) {
        log.warn("❌ Credit failed → compensating: {}", tx.getId());
        tx.setStatus(TransactionStatus.COMPENSATING);
        transactionRepository.save(tx);

        // Yêu cầu BalanceService hoàn tiền lại fromAccount
        outboxService.publishRefundRequested(tx);
    }

    private void handleRefundCompleted(Transaction tx) {
        log.info("Refund completed → mark failed: {}", tx.getId());
        tx.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(tx);
        outboxService.publishFailed(tx);
    }


}