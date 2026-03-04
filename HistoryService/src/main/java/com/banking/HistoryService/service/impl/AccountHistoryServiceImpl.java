package com.banking.HistoryService.service.impl;

import com.banking.HistoryService.dto.request.AccountHistoryCreateDTO;
import com.banking.HistoryService.dto.response.AccountHistoryResponseDTO;
import com.banking.HistoryService.entity.AccountHistory;
import com.banking.HistoryService.enums.AccountEventType;
import com.banking.HistoryService.mapper.AccountHistoryMapper;
import com.banking.HistoryService.repository.AccountHistoryRepository;
import com.banking.HistoryService.service.AccountHistoryService;
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
public class AccountHistoryServiceImpl implements AccountHistoryService {

    private final AccountHistoryRepository accountHistoryRepository;
    private final AccountHistoryMapper accountHistoryMapper;

    // ── WRITE ──────────────────────────────────────────────

    @Override
    @Transactional
    public void save(AccountHistoryCreateDTO dto) {
        AccountHistory entity = accountHistoryMapper.toEntity(dto);
        accountHistoryRepository.save(entity);
        log.info("✅ Account history saved: {} - {}", dto.getEventType(), dto.getAccountId());
    }

    // ── READ ───────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<AccountHistoryResponseDTO> getByAccountId(UUID accountId) {
        return accountHistoryMapper.toResponseDTOs(
                accountHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountHistoryResponseDTO> getByAccountId(UUID accountId, Pageable pageable) {
        return accountHistoryRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(accountHistoryMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountHistoryResponseDTO> getByAccountIdAndEventType(
            UUID accountId, AccountEventType eventType) {
        return accountHistoryMapper.toResponseDTOs(
                accountHistoryRepository.findByAccountIdAndEventTypeOrderByCreatedAtDesc(
                        accountId, eventType)
        );
    }
}