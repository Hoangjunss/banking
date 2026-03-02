package com.banking.BalanceService.controller;

import com.banking.BalanceService.dto.*;
import com.banking.BalanceService.entity.TransactionType;
import com.banking.BalanceService.service.BalanceHistoryService;
import com.banking.BalanceService.service.BalanceService;
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
@RequestMapping("/api/v1/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;
    private final BalanceHistoryService historyService;

    // 1. Lấy số dư hiện tại
    @GetMapping("/{accountId}")
    public ApiResponse<BalanceResponseDTO> getBalance(@PathVariable UUID accountId) {
        return ApiResponse.success(balanceService.getBalance(accountId));
    }

    // 2. Nạp tiền (Credit)
    @PostMapping("/credit")
    public ApiResponse<Void> credit(@RequestBody CreditRequestDTO request) {
        balanceService.credit(request);
        return ApiResponse.success();
    }

    // 3. Rút tiền (Debit)
    @PostMapping("/debit")
    public ApiResponse<Void> debit(@RequestBody DebitRequestDTO request) {
        balanceService.debit(request);
        return ApiResponse.success();
    }

    // 4. Xem lịch sử phân trang
    @GetMapping("/{accountId}/history")
    public ApiResponse<Page<BalanceHistoryResponseDTO>> getHistoryPaged(
            @PathVariable UUID accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(historyService.getHistoryPaged(accountId, pageable));
    }

    // 5. Lọc lịch sử theo khoảng thời gian
    @GetMapping("/{accountId}/history/filter-date")
    public ApiResponse<List<BalanceHistoryResponseDTO>> getHistoryByDate(
            @PathVariable UUID accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ApiResponse.success(historyService.getHistoryByDateRange(accountId, start, end));
    }

    // 6. Lọc theo loại giao dịch (CREDIT/DEBIT)
    @GetMapping("/{accountId}/history/filter-type")
    public ApiResponse<List<BalanceHistoryResponseDTO>> getHistoryByType(
            @PathVariable UUID accountId,
            @RequestParam TransactionType type) {

        return ApiResponse.success(historyService.getHistoryByType(accountId, type));
    }

    // 7. Truy vấn nhanh theo mã Giao dịch (Transaction ID)
    @GetMapping("/history/transaction/{transactionId}")
    public ApiResponse<BalanceHistoryResponseDTO> getByTxId(@PathVariable UUID transactionId) {
        return ApiResponse.success(historyService.getHistoryByTransactionId(transactionId));
    }
}