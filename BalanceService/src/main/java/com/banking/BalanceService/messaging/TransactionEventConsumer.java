package com.banking.BalanceService.messaging;

import com.banking.BalanceService.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventConsumer {

    private final BalanceService balanceService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "transaction-events",
            groupId = "balance-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTransactionEvent(ConsumerRecord<String, String> record) {
        log.info("Received: topic={}, partition={}, offset={}",
                record.topic(), record.partition(), record.offset());

        try {
            TransactionEventPayload payload = objectMapper.readValue(
                    record.value(),
                    TransactionEventPayload.class
            );

            switch (payload.getEventType()) {
                case "TRANSACTION_COMPLETED" -> handleCompleted(payload);
                case "TRANSACTION_FAILED"    -> handleFailed(payload);
                default -> log.warn("Unknown event type: {}", payload.getEventType());
            }

        } catch (JsonProcessingException e) {
            // Parse fail → không retry vì retry cũng fail
            // → log lại để xử lý thủ công
            log.error("Failed to parse event: {}", record.value(), e);
        }
    }

    private void handleCompleted(TransactionEventPayload payload) {
        log.info("Processing TRANSACTION_COMPLETED: {}", payload.getTransactionId());
        balanceService.applyTransaction(payload);
    }

    private void handleFailed(TransactionEventPayload payload) {
        log.info("Processing TRANSACTION_FAILED: {}", payload.getTransactionId());
        balanceService.revertTransaction(payload);
    }
}