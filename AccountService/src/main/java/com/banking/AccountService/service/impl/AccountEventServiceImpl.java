package com.banking.AccountService.service.impl;

import com.banking.AccountService.entity.AccountEvent;
import com.banking.AccountService.respository.AccountEventRepository;
import com.banking.AccountService.service.AccountEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class AccountEventServiceImpl implements AccountEventService {
    private final AccountEventRepository eventRepository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY) // Luôn chạy chung transaction với AccountService
    public void logEvent(UUID accountId, String eventType, String description) {
        AccountEvent event = new AccountEvent();
        event.setId(UUID.randomUUID());
        event.setAccountId(accountId);
        event.setEventType(eventType);
        event.setDescription(description);
        event.setCreatedAt(LocalDateTime.now());

        eventRepository.save(event);
    }
}
