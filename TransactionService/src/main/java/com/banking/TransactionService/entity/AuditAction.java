package com.banking.TransactionService.entity;

public enum AuditAction {

    CREATED,
    PROCESSING,

    DEBIT_REQUESTED,
    DEBIT_SUCCESS,
    DEBIT_FAILED,

    CREDIT_REQUESTED,
    CREDIT_SUCCESS,
    CREDIT_FAILED,

    COMPLETED,
    FAILED,
    REVERSED
}