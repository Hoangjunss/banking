package com.banking.HistoryService.repository;

import com.banking.HistoryService.entity.AccountHistory;
import com.banking.HistoryService.enums.AccountEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, UUID> {

    List<AccountHistory> findByAccountIdOrderByCreatedAtDesc(UUID accountId);

    Page<AccountHistory> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);

    List<AccountHistory> findByAccountIdAndEventTypeOrderByCreatedAtDesc(
            UUID accountId, AccountEventType eventType);
}