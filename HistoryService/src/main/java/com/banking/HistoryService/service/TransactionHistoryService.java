package com.banking.HistoryService.service;

import com.banking.HistoryService.dto.request.TransactionHistoryCreateDTO;
import com.banking.HistoryService.dto.response.TransactionHistoryResponseDTO;
import com.banking.HistoryService.enums.TransactionDirection;
import com.banking.HistoryService.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionHistoryService {

    // Ghi
    void save(TransactionHistoryCreateDTO dto);
    void saveAll(List<TransactionHistoryCreateDTO> dtos);

    // Đọc
    Page<TransactionHistoryResponseDTO> getByAccountId(UUID accountId, Pageable pageable);
    List<TransactionHistoryResponseDTO> getByTransactionId(UUID transactionId);
    Page<TransactionHistoryResponseDTO> getByAccountIdAndType(
            UUID accountId, TransactionType type, Pageable pageable);
    Page<TransactionHistoryResponseDTO> getByAccountIdAndDirection(
            UUID accountId, TransactionDirection direction, Pageable pageable);
}