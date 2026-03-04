package com.banking.HistoryService.repository;

import com.banking.HistoryService.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, UUID> {
}
