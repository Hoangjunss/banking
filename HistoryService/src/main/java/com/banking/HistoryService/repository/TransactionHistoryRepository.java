package com.banking.HistoryService.repository;

import com.banking.HistoryService.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, UUID> {
}
