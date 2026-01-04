package com.banking.TransactionService.dto.internal;

import java.time.Instant;

public class OutboxEventResponse {

    private String eventType;
    private String aggregateType;
    private String status;
    private Instant createdAt;
}
