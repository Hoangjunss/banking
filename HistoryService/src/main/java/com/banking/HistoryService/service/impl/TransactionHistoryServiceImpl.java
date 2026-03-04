package com.banking.HistoryService.service.impl;


import com.banking.HistoryService.dto.request.TransactionHistoryCreateDTO;
import com.banking.HistoryService.dto.response.TransactionHistoryResponseDTO;
import com.banking.HistoryService.entity.TransactionHistory;
import com.banking.HistoryService.enums.TransactionDirection;
import com.banking.HistoryService.enums.TransactionType;
import com.banking.HistoryService.mapper.TransactionHistoryMapper;
import com.banking.HistoryService.repository.TransactionHistoryRepository;
import com.banking.HistoryService.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper transactionHistoryMapper;

    // ── WRITE ──────────────────────────────────────────────

    @Override
    @Transactional
    public void save(TransactionHistoryCreateDTO dto) {
        transactionHistoryRepository.save(transactionHistoryMapper.toEntity(dto));
        log.info("✅ Transaction history saved: {} - {}", dto.getType(), dto.getTransactionId());
    }

    @Override
    @Transactional
    public void saveAll(List<TransactionHistoryCreateDTO> dtos) {
        List<TransactionHistory> entities = dtos.stream()
                .map(transactionHistoryMapper::toEntity)
                .toList();
        transactionHistoryRepository.saveAll(entities);
        log.info("✅ Transaction histories saved: {} records", entities.size());
    }

    // ── READ ───────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistoryResponseDTO> getByAccountId(UUID accountId, Pageable pageable) {
        return transactionHistoryRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(transactionHistoryMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDTO> getByTransactionId(UUID transactionId) {
        return transactionHistoryMapper.toResponseDTOs(
                transactionHistoryRepository.findByTransactionId(transactionId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistoryResponseDTO> getByAccountIdAndType(
            UUID accountId, TransactionType type, Pageable pageable) {
        return transactionHistoryRepository
                .findByAccountIdAndTypeOrderByCreatedAtDesc(accountId, type, pageable)
                .map(transactionHistoryMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistoryResponseDTO> getByAccountIdAndDirection(
            UUID accountId, TransactionDirection direction, Pageable pageable) {
        return transactionHistoryRepository
                .findByAccountIdAndDirectionOrderByCreatedAtDesc(accountId, direction, pageable)
                .map(transactionHistoryMapper::toResponseDTO);
    }
}