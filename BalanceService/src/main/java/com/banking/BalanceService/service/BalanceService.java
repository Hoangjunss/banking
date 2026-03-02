package com.banking.BalanceService.service;

import com.banking.BalanceService.dto.BalanceResponseDTO;
import com.banking.BalanceService.dto.CreditRequestDTO;
import com.banking.BalanceService.dto.DebitRequestDTO;

import java.util.UUID;

public interface BalanceService {

    /**
     * Lấy thông tin số dư hiện tại của tài khoản
     * @param accountId ID của tài khoản
     * @return DTO chứa số dư khả dụng và số dư đóng băng
     */
    BalanceResponseDTO getBalance(UUID accountId);

    /**
     * Nạp tiền vào tài khoản (Ghi có)
     * Xử lý cộng tiền và lưu lịch sử biến động
     * @param request DTO chứa thông tin nạp tiền và transactionId để đối soát
     */
    void credit(CreditRequestDTO request);

    /**
     * Rút tiền hoặc thanh toán từ tài khoản (Ghi nợ)
     * Kiểm tra số dư khả dụng trước khi thực hiện trừ tiền
     * @param request DTO chứa thông tin trừ tiền và transactionId để đối soát
     */
    void debit(DebitRequestDTO request);
}