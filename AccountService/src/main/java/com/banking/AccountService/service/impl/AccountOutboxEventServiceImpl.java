package com.banking.AccountService.service.impl;

import com.banking.AccountService.entity.AccountOutboxEvent;
import com.banking.AccountService.entity.OutboxStatus;
import com.banking.AccountService.respository.AccountOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.util.SerializationUtils.serialize;
@Service
@RequiredArgsConstructor
public class AccountOutboxEventServiceImpl {
    private final AccountOutboxEventRepository outboxRepository;

    @Transactional(propagation = Propagation.MANDATORY) // Phải chạy chung transaction với Account
    public void createEvent(UUID aggregateId, String type, Object payload) {
        AccountOutboxEvent event = new AccountOutboxEvent();
        event.setId(UUID.randomUUID());
        event.setAggregateId(aggregateId);
        event.setType(type);
        event.setPayload(serialize(payload));
        event.setStatus(OutboxStatus.PENDING);
        outboxRepository.save(event);
    }
}
}
