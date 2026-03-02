package com.banking.BalanceService.service;

import com.banking.BalanceService.dto.BalanceHistoryResponseDTO;
import com.banking.BalanceService.entity.TransactionType;
import com.banking.BalanceService.mapper.BalanceMapper;
import com.banking.BalanceService.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    private final BalanceHistoryRepository historyRepository;
    private final BalanceMapper balanceMapper;

    @Override
    public Page<BalanceHistoryResponseDTO> getHistoryPaged(UUID accountId, Pageable pageable) {
        return historyRepository.findByAccountId(accountId, pageable)
                .map(balanceMapper::toHistoryResponseDTO);
    }

    @Override
    public List<BalanceHistoryResponseDTO> getHistoryByDateRange(UUID accountId, LocalDateTime start, LocalDateTime end) {
        return historyRepository.findByAccountIdAndCreatedAtBetween(accountId, start, end)
                .stream()
                .map(balanceMapper::toHistoryResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BalanceHistoryResponseDTO> getHistoryByType(UUID accountId, TransactionType type) {
        return null;
    }

    @Override
    public BalanceHistoryResponseDTO getHistoryByTransactionId(UUID transactionId) {
        return historyRepository.findByTransactionId(transactionId)
                .map(balanceMapper::toHistoryResponseDTO)
                .orElseThrow(() -> new RuntimeException("Giao dịch không tồn tại"));
    }

    @Override
    public void generateMonthlyReport(UUID accountId, int month, int year) {

    }

    @Override
    public boolean verifyBalanceIntegrity(UUID accountId) {
        // Logic: Lấy số dư đầu kỳ + (Tổng Credit) - (Tổng Debit) = Số dư hiện tại?
        // Đây là tính năng bảo mật cao trong các hệ thống Core Banking
        return true;
    }
}