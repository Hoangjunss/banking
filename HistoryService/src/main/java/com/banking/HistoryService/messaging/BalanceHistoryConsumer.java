package com.banking.HistoryService.messaging;

import com.banking.HistoryService.dto.internal.SagaEvent;
import com.banking.HistoryService.dto.request.BalanceHistoryCreateDTO;
import com.banking.HistoryService.enums.BalanceChangeType;
import com.banking.HistoryService.service.BalanceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BalanceHistoryConsumer {
    private final BalanceHistoryService balanceHistoryService;
    private final ObjectMapper objectMapper;


    @KafkaListener(
            topics = "balance-events",
            groupId = "history-service"
    )
    public void onBalanceEvent(ConsumerRecord<String, String> record) {
        try {
            SagaEvent event = objectMapper.readValue(record.value(), SagaEvent.class);

            log.info("Balance event received: {} - {}", event.getType(), event.getTransactionId());

            switch (event.getType()) {
                case "DEBIT_COMPLETED"   -> save(event, BalanceChangeType.DEBIT);
                case "CREDIT_COMPLETED"  -> save(event, BalanceChangeType.CREDIT);
                case "REFUND_COMPLETED"  -> save(event, BalanceChangeType.CREDIT);
                // FAILED → không lưu vào history, tiền không thay đổi
                default -> log.debug("Skipped balance event: {}", event.getType());
            }

        } catch (Exception e) {
            log.error("Failed to parse balance event: {}", record.value(), e);
        }
    }

    private void save(SagaEvent event, BalanceChangeType type) {
        BalanceHistoryCreateDTO dto = BalanceHistoryCreateDTO.builder()
                .accountId(UUID.fromString(event.getPayload().getInitiatedBy()))
                .transactionId(UUID.fromString(event.getTransactionId()))
                .type(type)
                .amount(event.getPayload().getAmount())
                .build();

        balanceHistoryService.save(dto);
        log.info("✅ Balance history saved: {} account={}",
                type,
                event.getPayload().getInitiatedBy());
    }
}