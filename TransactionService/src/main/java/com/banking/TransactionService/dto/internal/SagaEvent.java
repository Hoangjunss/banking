package com.banking.TransactionService.dto.internal;

import lombok.Data;

@Data
public class SagaEvent {
    private String transactionId;
    private String type;      // DEBIT_COMPLETED | DEBIT_FAILED | CREDIT_COMPLETED | CREDIT_FAILED | REFUND_COMPLETED
    private String reason;    // "Insufficient funds" nếu fail
}