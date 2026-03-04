package com.banking.HistoryService.dto.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransactionEventPayload {
    private UUID transactionId;

    private String aggregateType;
    private UUID aggregateId;
    private String eventType;

    private String initiatedBy;
    private String fromAccountId;
    private String toAccountId;

    private BigDecimal amount;
    private String type;
    private String referenceCode;
}
