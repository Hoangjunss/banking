package com.banking.TransactionService.messaging;

import com.banking.TransactionService.entity.OutboxStatus;
import com.banking.TransactionService.entity.TransactionOutboxEvent;
import com.banking.TransactionService.repository.TransactionOutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionOutboxMessageRelay {

    private final TransactionOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutboxEvents() {
        List<TransactionOutboxEvent> events = outboxRepository
                .findByStatusInOrderByCreatedAtAsc(
                        List.of(OutboxStatus.PENDING, OutboxStatus.FAILED)
                );

        for (TransactionOutboxEvent event : events) {
            try {
                String topic = event.getAggregateType().toLowerCase() + "-events";
                // "TRANSACTION" → "transaction-events"

                kafkaTemplate.send(
                        topic,
                        event.getAggregateId().toString(),
                        event.getPayload()
                ).get();

                event.setStatus(OutboxStatus.SENT);
                log.info("Sent: {} - {}", event.getEventType(), event.getId());

            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                log.error("Failed: {}", event.getId(), e);
            }

            outboxRepository.save(event);
        }
    }
}