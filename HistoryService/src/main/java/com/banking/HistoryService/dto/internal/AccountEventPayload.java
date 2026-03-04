package com.banking.HistoryService.dto.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountEventPayload {

    private UUID aggregateId;      // accountId
    private String eventType;      // ACCOUNT_CREATED | ACCOUNT_FROZEN...
    private String aggregateType;  // ACCOUNT

    // thông tin account
    private UUID ownerId;
    private String accountNumber;
    private String performedBy;
    private String detail;
    private Instant eventAt;
}