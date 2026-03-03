package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.dto.request.*;
import com.banking.TransactionService.dto.response.TransactionResponseDTO;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionStatus;
import com.banking.TransactionService.mapper.TransactionMapper;
import com.banking.TransactionService.repository.TransactionRepository;
import com.banking.TransactionService.service.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * =============================================================
 * Transaction Application Service (Orchestrator)
 * =============================================================
 *
 * Responsibilities:
 * - Manage Transaction aggregate lifecycle
 * - Create accounting ledger entries
 * - Trigger integration events via Outbox
 * - Orchestrate Saga for distributed operations (TRANSFER)
 *
 * Patterns used:
 * - Transactional Outbox
 * - Saga (orchestrator-based)
 * - Double-entry bookkeeping
 *
 * Rules:
 * - DEPOSIT/WITHDRAW → local → complete immediately
 * - TRANSFER → distributed → Saga → async completion
 *
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEntryService entryService;
    private final TransactionOutboxService outboxService;
    private final TransactionAuditService auditService;
    private final TransactionMapper mapper;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            TransactionEntryService entryService,
            TransactionOutboxService outboxService,
            TransactionAuditService auditService,
            TransactionMapper mapper
    ) {
        this.transactionRepository = transactionRepository;
        this.entryService = entryService;
        this.outboxService = outboxService;
        this.auditService = auditService;
        this.mapper = mapper;
    }

    /* =====================================================
       DEPOSIT
       -----------------------------------------------------
       Single account → local DB only
       → no Saga required
       → safe to complete immediately
       ===================================================== */

    @Override
    public TransactionResponseDTO deposit(DepositRequestDTO request) {

        // 1. Create aggregate
        Transaction tx = mapper.toDepositEntity(request);
        enrich(tx);

        transactionRepository.save(tx);

        // 2. Create credit ledger entry
        entryService.createDepositEntries(tx, request);

        // 3. Mark completed immediately (local operation)
        markSuccess(tx);

        // 4. Audit + publish integration event
        auditService.logCompleted(tx);
        outboxService.publishCompleted(tx);

        return mapper.toResponse(tx);
    }

    /* =====================================================
       WITHDRAW
       -----------------------------------------------------
       Single account → local DB only
       → no Saga required
       ===================================================== */

    @Override
    public TransactionResponseDTO withdraw(WithdrawRequestDTO request) {

        Transaction tx = mapper.toWithdrawEntity(request);
        enrich(tx);

        transactionRepository.save(tx);

        entryService.createWithdrawEntries(tx, request);

        // complete immediately
        markSuccess(tx);

        auditService.logCompleted(tx);
        outboxService.publishCompleted(tx);

        return mapper.toResponse(tx);
    }

    /* =====================================================
       TRANSFER  🔥 SAGA ENTRY POINT
       -----------------------------------------------------
       Two accounts → cross service → distributed
       → MUST use Saga
       → cannot mark SUCCESS here
       -----------------------------------------------------
       Flow:
       1) status = PROCESSING
       2) publish DEBIT_REQUESTED
       3) BalanceService executes debit
       4) later Kafka callback completes saga
       ===================================================== */

    @Override
    public TransactionResponseDTO transfer(TransferRequestDTO request) {

        Transaction tx = mapper.toTransferEntity(request);
        enrich(tx);

        // 🔥 IMPORTANT:
        // do NOT mark success here
        // money not moved yet
        tx.setStatus(TransactionStatus.PROCESSING);

        transactionRepository.save(tx);

        // create double-entry bookkeeping records
        entryService.createTransferEntries(tx, request);

        // audit creation only
        auditService.logCreated(tx);

        // 🔥 Start Saga by requesting debit from BalanceService
        outboxService.publishDebitRequested(tx);

        return mapper.toResponse(tx);
    }

    /* =====================================================
       Helpers
       ===================================================== */

    /**
     * Enrich system-managed fields.
     * Ensures all transactions have a public-safe reference.
     */
    private void enrich(Transaction tx) {
        tx.setReferenceCode(generateReference());
    }

    /**
     * Mark transaction as SUCCESS and set completion timestamp.
     * Used only for local operations or final Saga step.
     */
    private void markSuccess(Transaction tx) {
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setCompletedAt(Instant.now());
    }

    /**
     * Generate external-safe reference code.
     * Never expose internal DB ID to clients.
     */
    private String generateReference() {
        return "TX-" + UUID.randomUUID();
    }
}