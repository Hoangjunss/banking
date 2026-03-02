package com.banking.BalanceService.controller;


import com.banking.BalanceService.dto.ApiResponse;
import com.banking.BalanceService.dto.BalanceHistoryResponseDTO;
import com.banking.BalanceService.entity.TransactionType;
import com.banking.BalanceService.service.BalanceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/balance-histories")
@RequiredArgsConstructor
public class BalanceHistoryController {

    private final BalanceHistoryService historyService;

    /**
     * Lấy lịch sử giao dịch phân trang
     * URL ví dụ: /api/v1/balance-histories/uuid-cua-ban?page=0&size=20
     */
    @GetMapping("/{accountId}")
    public ApiResponse<Page<BalanceHistoryResponseDTO>> getHistoryPaged(
            @PathVariable UUID accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(historyService.getHistoryPaged(accountId, pageable));
    }

    /**
     * Lọc lịch sử theo khoảng thời gian
     * URL ví dụ: /api/v1/balance-histories/uuid/filter-date?start=2024-01-01T00:00:00&end=2024-01-31T23:59:59
     */
    @GetMapping("/{accountId}/filter-date")
    public ApiResponse<List<BalanceHistoryResponseDTO>> getHistoryByDate(
            @PathVariable UUID accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ApiResponse.success(historyService.getHistoryByDateRange(accountId, start, end));
    }

    /**
     * Lọc theo loại giao dịch (CREDIT/DEBIT)
     */
    @GetMapping("/{accountId}/filter-type")
    public ApiResponse<List<BalanceHistoryResponseDTO>> getHistoryByType(
            @PathVariable UUID accountId,
            @RequestParam TransactionType type) {

        return ApiResponse.success(historyService.getHistoryByType(accountId, type));
    }

    /**
     * Tra cứu một giao dịch cụ thể qua TransactionID (Dùng cho đối soát)
     */
    @GetMapping("/transaction/{transactionId}")
    public ApiResponse<BalanceHistoryResponseDTO> getByTransactionId(@PathVariable UUID transactionId) {
        return ApiResponse.success(historyService.getHistoryByTransactionId(transactionId));
    }

    /**
     * Kiểm tra tính toàn vẹn của số dư (Audit)
     */
    @GetMapping("/{accountId}/verify")
    public ApiResponse<Boolean> verifyIntegrity(@PathVariable UUID accountId) {
        return ApiResponse.success(historyService.verifyBalanceIntegrity(accountId));
    }
}