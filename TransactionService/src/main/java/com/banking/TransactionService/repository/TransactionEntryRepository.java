package com.banking.TransactionService.repository;

import com.banking.TransactionService.entity.TransactionEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionEntryRepository extends JpaRepository<TransactionEntry, UUID> {

    List<TransactionEntry> findByTransactionId(UUID transactionId);
}