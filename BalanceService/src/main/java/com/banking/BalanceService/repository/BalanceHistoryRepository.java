package com.banking.BalanceService.repository;

import com.banking.BalanceService.entity.BalanceHistory;
import com.banking.BalanceService.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, UUID> {
    // Tìm kiếm phân trang
    Page<BalanceHistory> findByAccountId(UUID accountId, Pageable pageable);

    // Tìm kiếm theo khoảng thời gian
    List<BalanceHistory> findByAccountIdAndCreatedAtBetween(UUID accountId, LocalDateTime start, LocalDateTime end);

    // Tìm theo transactionId
    Optional<BalanceHistory> findByTransactionId(UUID transactionId);

    // Tìm theo loại giao dịch
    List<BalanceHistory> findByAccountIdAndType(UUID accountId, TransactionType type);

    boolean existsByTransactionId(UUID transactionId);
}
