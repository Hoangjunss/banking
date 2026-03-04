package com.banking.HistoryService.repository;

import com.banking.HistoryService.entity.TransactionHistory;
import com.banking.HistoryService.enums.TransactionDirection;
import com.banking.HistoryService.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, UUID> {

    Page<TransactionHistory> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);

    List<TransactionHistory> findByTransactionId(UUID transactionId);

    Page<TransactionHistory> findByAccountIdAndTypeOrderByCreatedAtDesc(
            UUID accountId, TransactionType type, Pageable pageable);

    Page<TransactionHistory> findByAccountIdAndDirectionOrderByCreatedAtDesc(
            UUID accountId, TransactionDirection direction, Pageable pageable);
}