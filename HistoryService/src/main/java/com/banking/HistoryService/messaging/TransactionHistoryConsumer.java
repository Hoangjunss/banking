package com.banking.HistoryService.messaging;

import com.banking.HistoryService.dto.internal.TransactionEventPayload;
import com.banking.HistoryService.dto.request.TransactionHistoryCreateDTO;
import com.banking.HistoryService.enums.TransactionStatus;
import com.banking.HistoryService.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionHistoryConsumer {
    private final TransactionHistoryService transactionHistoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "transaction-events", groupId = "history-service")
    public void onTransactionEvent(ConsumerRecord<String, String> record) {
        try {
            TransactionEventPayload payload = objectMapper.readValue(
                    record.value(), TransactionEventPayload.class);

            switch (payload.getEventType()) {
                case "TRANSACTION_COMPLETED" -> saveHistories(payload, TransactionStatus.SUCCESS);
                case "TRANSACTION_FAILED"    -> saveHistories(payload, TransactionStatus.FAILED);
                default -> log.debug("Skipped: {}", payload.getEventType());
            }
        } catch (Exception e) {
            log.error("Failed to parse transaction event: {}", record.value(), e);
        }
    }

    private void saveHistories(TransactionEventPayload payload, TransactionStatus status) {
//        List<TransactionHistoryCreateDTO> dtos = buildDTOs(payload, status);
//        transactionHistoryService.saveAll(dtos);
    }
}
