package com.banking.BalanceService.messaging;

import com.banking.BalanceService.dto.CreditRequestDTO;
import com.banking.BalanceService.dto.DebitRequestDTO;
import com.banking.BalanceService.dto.internal.SagaEvent;
import com.banking.BalanceService.dto.internal.TransactionEventPayload;
import com.banking.BalanceService.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventConsumer {

    private final BalanceService balanceService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(
            topics = "transaction-events",
            groupId = "balance-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTransactionEvent(ConsumerRecord<String, String> record) {
        log.info("Received: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

        String message = record.value();

        try {
            TransactionEventPayload payload =
                    objectMapper.readValue(message, TransactionEventPayload.class);

            handleEvent(payload);

        } catch (Exception  e) {
            // Parse fail → không retry vì retry cũng fail
            // → log lại để xử lý thủ công
            log.error("Failed to parse event: {}", record.value(), e);
        }
    }

    private void handleEvent(TransactionEventPayload payload) {

        switch (payload.getEventType()) {

            case "DEBIT_REQUESTED" -> handleDebit(payload);

            case "CREDIT_REQUESTED" -> handleCredit(payload);

            case "TRANSACTION_COMPLETED" -> handleCompleted(payload);

            case "TRANSACTION_FAILED" -> handleFailed(payload);

            case "REFUND_REQUESTED" -> handleRefund(payload);

            default -> throw new IllegalArgumentException(
                        "Unknown event type: " + payload.getEventType()
                );
        }
    }

    private void handleDebit(TransactionEventPayload payload) {
        try {
            DebitRequestDTO request = DebitRequestDTO.builder()
                    .transactionId(payload.getTransactionId())
                    .accountId(UUID.fromString(payload.getFromAccountId()))
                    .amount(payload.getAmount())
                    .build();

            balanceService.debit(request);

            // ✅ Debit thành công → báo TransactionService
            publishBalanceEvent(payload.getTransactionId(), "DEBIT_COMPLETED", null);
            log.info("✅ Debit completed: {}", payload.getTransactionId());

        } catch (Exception e) {
            // ❌ Không đủ số dư hoặc lỗi khác → báo fail
            publishBalanceEvent(payload.getTransactionId(), "DEBIT_FAILED", e.getMessage());
            log.warn("❌ Debit failed: {} - {}", payload.getTransactionId(), e.getMessage());
        }
    }

    private void handleCredit(TransactionEventPayload payload) {
        try {
            CreditRequestDTO request = CreditRequestDTO.builder()
                    .transactionId(payload.getTransactionId())
                    .accountId(UUID.fromString(payload.getToAccountId()))
                    .amount(payload.getAmount())
                    .build();

            balanceService.credit(request);

            // ✅ Credit thành công
            publishBalanceEvent(payload.getTransactionId(), "CREDIT_COMPLETED", null);
            log.info("✅ Credit completed: {}", payload.getTransactionId());

        } catch (Exception e) {
            // ❌ Credit fail → cần hoàn tiền lại
            publishBalanceEvent(payload.getTransactionId(), "CREDIT_FAILED", e.getMessage());
            log.warn("❌ Credit failed: {} - {}", payload.getTransactionId(), e.getMessage());
        }
    }

    private void handleCompleted(TransactionEventPayload payload) {
        // update trạng thái
    }

    private void handleFailed(TransactionEventPayload payload) {
        // rollback hoặc mark fail
    }

    private void handleRefund(TransactionEventPayload payload) {
        try {
            CreditRequestDTO request = CreditRequestDTO.builder()
                    .transactionId(payload.getTransactionId())
                    .accountId(UUID.fromString(payload.getFromAccountId()))
                    .amount(payload.getAmount())
                    .build();

            // Dùng credit để hoàn tiền lại fromAccount
            balanceService.credit(request);

            publishBalanceEvent(payload.getTransactionId(), "REFUND_COMPLETED", null);
            log.info("✅ Refund completed: {}", payload.getTransactionId());

        } catch (Exception e) {
            // Refund fail → cần alert team xử lý thủ công
            log.error("💀 Refund FAILED - manual intervention required: {}",
                    payload.getTransactionId(), e);
        }
    }

    // ── Helper publish về balance-events ──────────────────
    private void publishBalanceEvent(UUID transactionId, String type, String reason) {
        try {
            SagaEvent event = SagaEvent.builder()
                    .transactionId(transactionId.toString())
                    .type(type)
                    .reason(reason)
                    .build();

            kafkaTemplate.send(
                    "balance-events",
                    transactionId.toString(),
                    objectMapper.writeValueAsString(event)
            );
        } catch (Exception e) {
            log.error("Failed to publish balance event", e);
        }
    }
}