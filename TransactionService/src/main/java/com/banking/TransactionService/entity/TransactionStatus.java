package com.banking.TransactionService.entity;

public enum TransactionStatus {

    PENDING,    // Mới tạo, đang xử lý
    SUCCESS,    // Hoàn tất thành công
    FAILED,     // Thất bại (validation, balance…)
    REVERSED    // Đã rollback / hoàn tiền
}