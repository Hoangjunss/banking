package com.banking.BalanceService.repository;

import com.banking.BalanceService.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, UUID> {
    // Tìm số dư dựa trên ID tài khoản
    Optional<Balance> findByAccountId(UUID accountId);
}