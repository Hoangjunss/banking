package com.banking.HistoryService.enums;

import lombok.Getter;

@Getter
public enum TransactionType {

    TRANSFER,
    DEPOSIT,
    WITHDRAW,
    PAYMENT
}