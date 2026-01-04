package com.banking.TransactionService.dto.internal;

import java.time.Instant;

public class OutboxEventResponseDTO {

    private String eventType;
    private String aggregateType;
    private String status;
    private Instant createdAt;
}
