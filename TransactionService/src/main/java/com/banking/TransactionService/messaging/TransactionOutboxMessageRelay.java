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

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionOutboxMessageRelay {

    private final TransactionOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final int MAX_RETRY = 5;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutboxEvents() {
        List<TransactionOutboxEvent> events = outboxRepository
                .findByStatusInOrderByCreatedAtAsc(
                        List.of(OutboxStatus.PENDING, OutboxStatus.FAILED)
                );
        if (events.isEmpty()) return;

        log.info("Processing {} outbox events", events.size());

        /* ==============================
           mark PROCESSING first (lock)
           ============================== */
        events.forEach(e -> e.setStatus(OutboxStatus.PROCESSING));
        outboxRepository.saveAll(events);

        /* ==============================
           send kafka async
           ============================== */
        for (TransactionOutboxEvent event : events) {

            String topic = buildTopic(event);

            try {
                kafkaTemplate.send(
                        topic,
                        event.getAggregateId().toString(),
                        event.getPayload()
                );

                event.setStatus(OutboxStatus.SENT);

                log.info("✅ Sent {} - {}", event.getEventType(), event.getId());

            } catch (Exception ex) {

                int retry = event.getRetryCount() + 1;
                event.setRetryCount(retry);

                if (retry >= MAX_RETRY) {
                    log.error("❌ Max retry reached: {}", event.getId());
                    event.setStatus(OutboxStatus.FAILED);
                } else {
                    event.setStatus(OutboxStatus.FAILED);
                    event.setNextRetryAt(Instant.now().plusSeconds(10 * retry));
                }
            }
        }

        outboxRepository.saveAll(events);
    }

    /* ==============================
       helper
       ============================== */

    private String buildTopic(TransactionOutboxEvent e) {
        return e.getAggregateType().toLowerCase() + "-events";
    }

}