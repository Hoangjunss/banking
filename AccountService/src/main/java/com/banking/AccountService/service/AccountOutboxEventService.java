package com.banking.AccountService.service;

import java.util.UUID;

public interface AccountOutboxEventService {
    void createEvent(UUID aggregateId, String aggregateType, String type, Object payload);
}
