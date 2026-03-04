package com.banking.HistoryService.messaging;

import com.banking.HistoryService.dto.internal.AccountEventPayload;
import com.banking.HistoryService.dto.request.AccountHistoryCreateDTO;
import com.banking.HistoryService.enums.AccountEventType;
import com.banking.HistoryService.service.AccountHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountHistoryConsumer {

    private final AccountHistoryService accountHistoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "account-events",
            groupId = "history-service"
    )
    public void onAccountEvent(ConsumerRecord<String, String> record) {
        log.info("Account event received: partition={}, offset={}",
                record.partition(), record.offset());
        try {
            AccountEventPayload payload = objectMapper.readValue(
                    record.value(), AccountEventPayload.class
            );

            // Bỏ qua event không liên quan
            if (!isSupportedEvent(payload.getEventType())) {
                log.debug("Skipped account event: {}", payload.getEventType());
                return;
            }

            AccountHistoryCreateDTO dto = AccountHistoryCreateDTO.builder()
                    .accountId(payload.getAggregateId())
                    .ownerId(payload.getOwnerId())
                    .eventType(AccountEventType.valueOf(payload.getEventType()))
                    .detail(payload.getDetail())
                    .performedBy(
                            payload.getPerformedBy() != null
                                    ? payload.getPerformedBy()
                                    : "System"
                    )
                    .eventAt(
                            payload.getEventAt() != null
                                    ? payload.getEventAt()
                                    : Instant.now()
                    )
                    .build();

            accountHistoryService.save(dto);

        } catch (Exception e) {
            log.error("Failed to parse account event: {}", record.value(), e);
        }
    }

    private boolean isSupportedEvent(String eventType) {
        try {
            AccountEventType.valueOf(eventType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}