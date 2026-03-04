package com.banking.HistoryService.enums;

import lombok.Getter;

@Getter
public enum AccountEventType {
    ACCOUNT_CREATED,
    ACCOUNT_FROZEN,
    ACCOUNT_UNFROZEN,
    ACCOUNT_CLOSED,
    LIMIT_CHANGED
}