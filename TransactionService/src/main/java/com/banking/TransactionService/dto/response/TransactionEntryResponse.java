package com.banking.TransactionService.dto.response;

import java.math.BigDecimal;

public class TransactionEntryResponse {

    private String accountId;
    private String entryType; // DEBIT / CREDIT
    private BigDecimal amount;
}