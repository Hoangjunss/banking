package com.banking.HistoryService.repository;

import com.banking.HistoryService.entity.BalanceHistory;
import com.banking.HistoryService.enums.BalanceChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, UUID> {

    Page<BalanceHistory> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);

    List<BalanceHistory> findByTransactionId(UUID transactionId);

    Page<BalanceHistory> findByAccountIdAndTypeOrderByCreatedAtDesc(
            UUID accountId, BalanceChangeType type, Pageable pageable);
}