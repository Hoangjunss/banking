package com.banking.HistoryService.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {

    SUCCESS,

    FAILED,

    REVERSED,

    COMPENSATING
}