package com.banking.HistoryService.service;

import com.banking.HistoryService.dto.request.BalanceHistoryCreateDTO;
import com.banking.HistoryService.dto.response.BalanceHistoryResponseDTO;
import com.banking.HistoryService.enums.BalanceChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BalanceHistoryService {

    // Ghi
    void save(BalanceHistoryCreateDTO dto);

    // Đọc
    Page<BalanceHistoryResponseDTO> getByAccountId(UUID accountId, Pageable pageable);
    List<BalanceHistoryResponseDTO> getByTransactionId(UUID transactionId);
    Page<BalanceHistoryResponseDTO> getByAccountIdAndType(
            UUID accountId, BalanceChangeType type, Pageable pageable);
}