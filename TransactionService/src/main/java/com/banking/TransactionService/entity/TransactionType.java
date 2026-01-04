package com.banking.TransactionService.entity;

public enum TransactionType {

    TRANSFER,   // Chuyển tiền nội bộ
    DEPOSIT,    // Nạp tiền
    WITHDRAW,   // Rút tiền
    PAYMENT     // Thanh toán (bill, merchant)
}