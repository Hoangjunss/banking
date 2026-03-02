package com.banking.TransactionService.dto.internal;

import lombok.Data;

@Data
public class SagaEvent {

    private String type;
    private String transactionId;
}