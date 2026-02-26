package com.banking.AccountService.service.impl;

import com.banking.AccountService.entity.AccountOutboxEvent;
import com.banking.AccountService.entity.OutboxStatus;
import com.banking.AccountService.respository.AccountOutboxEventRepository;
import com.banking.AccountService.service.AccountOutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.util.SerializationUtils.serialize;

@Service
@RequiredArgsConstructor
public class AccountOutboxEventServiceImpl implements AccountOutboxEventService {
    private final AccountOutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper; // inject Jackson ObjectMapper
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional(propagation = Propagation.MANDATORY)
    public void createEvent(UUID aggregateId, String aggregateType, String type, Object payload) {
        AccountOutboxEvent event = new AccountOutboxEvent();
        event.setId(UUID.randomUUID());
        event.setAggregateId(aggregateId);
        event.setEventType(type);
        event.setPayload(serialize(payload));
        event.setStatus(OutboxStatus.PENDING);
        event.setCreatedAt(LocalDateTime.now());
        outboxRepository.save(event);
    }

    private String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Failed to serialize outbox payload", e);
        }
    }
}