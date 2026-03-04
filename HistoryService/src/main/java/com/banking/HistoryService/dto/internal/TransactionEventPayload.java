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
    private String eventType;      // DEBIT_REQUESTED | CREDIT_REQUESTED
    // TRANSACTION_COMPLETED | TRANSACTION_FAILED
    private String type;           // DEPOSIT | WITHDRAW | TRANSFER
    private BigDecimal amount;
    private String fromAccountId;
    private String toAccountId;
    private String referenceCode;
    private String initiatedBy;
    private Instant completedAt;
    private String description;
}