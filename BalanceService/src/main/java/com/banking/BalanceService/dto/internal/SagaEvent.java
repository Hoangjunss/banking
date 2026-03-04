package com.banking.BalanceService.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SagaEvent {
    private String transactionId;
    private String type;      // DEBIT_COMPLETED | DEBIT_FAILED | CREDIT_COMPLETED | CREDIT_FAILED | REFUND_COMPLETED
    private String reason;    // "Insufficient funds" nếu fail
    private TransactionEventPayload payload;

    private String accountId;       // account bị ảnh hưởng trong event này
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
}