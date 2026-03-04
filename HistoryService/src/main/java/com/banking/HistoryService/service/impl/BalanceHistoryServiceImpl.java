package com.banking.HistoryService.service.impl;

import com.banking.HistoryService.dto.request.BalanceHistoryCreateDTO;
import com.banking.HistoryService.dto.response.BalanceHistoryResponseDTO;
import com.banking.HistoryService.enums.BalanceChangeType;
import com.banking.HistoryService.mapper.BalanceHistoryMapper;
import com.banking.HistoryService.repository.BalanceHistoryRepository;
import com.banking.HistoryService.service.BalanceHistoryService;
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
public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;
    private final BalanceHistoryMapper balanceHistoryMapper;

    // ── WRITE ──────────────────────────────────────────────

    @Override
    @Transactional
    public void save(BalanceHistoryCreateDTO dto) {
        balanceHistoryRepository.save(balanceHistoryMapper.toEntity(dto));
        log.info("✅ Balance history saved: {} - {}", dto.getType(), dto.getTransactionId());
    }

    // ── READ ───────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<BalanceHistoryResponseDTO> getByAccountId(UUID accountId, Pageable pageable) {
        return balanceHistoryRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(balanceHistoryMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BalanceHistoryResponseDTO> getByTransactionId(UUID transactionId) {
        return balanceHistoryMapper.toResponseDTOs(
                balanceHistoryRepository.findByTransactionId(transactionId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BalanceHistoryResponseDTO> getByAccountIdAndType(
            UUID accountId, BalanceChangeType type, Pageable pageable) {
        return balanceHistoryRepository
                .findByAccountIdAndTypeOrderByCreatedAtDesc(accountId, type, pageable)
                .map(balanceHistoryMapper::toResponseDTO);
    }
}