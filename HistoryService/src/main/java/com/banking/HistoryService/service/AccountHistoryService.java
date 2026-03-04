package com.banking.HistoryService.service;

import com.banking.HistoryService.dto.request.AccountHistoryCreateDTO;
import com.banking.HistoryService.dto.response.AccountHistoryResponseDTO;
import com.banking.HistoryService.enums.AccountEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountHistoryService {
    // Ghi
    void save(AccountHistoryCreateDTO dto);

    // Đọc
    List<AccountHistoryResponseDTO> getByAccountId(UUID accountId);
    Page<AccountHistoryResponseDTO> getByAccountId(UUID accountId, Pageable pageable);
    List<AccountHistoryResponseDTO> getByAccountIdAndEventType(UUID accountId, AccountEventType eventType);
}
