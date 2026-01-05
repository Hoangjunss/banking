package com.banking.AccountService.service;

import java.util.UUID;

public interface AccountEventService {
    void logEvent(UUID accountId, String eventType, String description);
}
