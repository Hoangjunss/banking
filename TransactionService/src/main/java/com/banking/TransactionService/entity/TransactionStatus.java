package com.banking.TransactionService.entity;

/**
 * Transaction lifecycle states.
 *
 * NOTE:
 * - Local operations (deposit/withdraw) go directly → SUCCESS
 * - Distributed operations (transfer) use Saga → PROCESSING first
 */
public enum TransactionStatus {

    /**
     * Transaction created but not processed yet.
     * (rarely visible, mostly internal)
     */
    PENDING,

    /**
     * Saga in progress.
     * Waiting for debit/credit from BalanceService.
     */
    PROCESSING,

    /**
     * Money movement completed successfully.
     * Final state.
     */
    SUCCESS,

    /**
     * Business failure.
     * Examples:
     * - insufficient balance
     * - validation failed
     */
    FAILED,

    /**
     * Compensation executed.
     * Money already deducted but reverted.
     * (Saga rollback)
     */
    REVERSED
}