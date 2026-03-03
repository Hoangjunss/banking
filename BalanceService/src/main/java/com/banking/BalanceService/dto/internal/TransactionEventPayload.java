package com.banking.BalanceService.dto.internal;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
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
