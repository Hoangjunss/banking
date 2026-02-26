package com.banking.AccountService.messaging;

import com.banking.AccountService.entity.AccountOutboxEvent;
import com.banking.AccountService.entity.OutboxStatus;
import com.banking.AccountService.respository.AccountOutboxEventRepository;
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
public class AccountOutboxMessageRelay {
    private final AccountOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processOutboxEvents() {
        List<AccountOutboxEvent> events = outboxRepository
                .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        for (AccountOutboxEvent event : events) {
            try {
                String topic = event.getAggregateType().toLowerCase() + "-events";

                kafkaTemplate.send(
                        topic,
                        event.getAggregateId().toString(),
                        event.getPayload()
                ).get(); // blocking → đảm bảo gửi xong mới tiếp

                event.setStatus(OutboxStatus.SENT);
                log.info("Sent outbox event: {} - {}", event.getEventType(), event.getId());

            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                log.error("Failed to send outbox event: {}", event.getId(), e);
            }

            outboxRepository.save(event);
        }
    }
}
