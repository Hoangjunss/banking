package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.dto.request.DepositRequestDTO;
import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.request.WithdrawRequestDTO;
import com.banking.TransactionService.dto.response.TransactionResponseDTO;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionStatus;
import com.banking.TransactionService.entity.TransactionType;
import com.banking.TransactionService.mapper.TransactionMapper;
import com.banking.TransactionService.repository.TransactionRepository;
import com.banking.TransactionService.service.OutboxService;
import com.banking.TransactionService.service.TransactionAuditService;
import com.banking.TransactionService.service.TransactionEntryService;
import com.banking.TransactionService.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Application service responsible for orchestrating transaction use cases.
 * Responsibilities:
 * - create and manage Transaction aggregate lifecycle
 * - delegate accounting logic to TransactionEntryService
 * - delegate integration events to OutboxService
 * - ensure transactional consistency
 */@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEntryService entryService;
    private final OutboxService outboxService;
    private final TransactionAuditService auditService;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            TransactionEntryService entryService,
            OutboxService outboxService,
            TransactionAuditService auditService,
            TransactionMapper transactionMapper
    ) {
        this.transactionRepository = transactionRepository;
        this.entryService = entryService;
        this.outboxService = outboxService;
        this.auditService = auditService;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Handle DEPOSIT transaction.
     *
     * <p>Money flows INTO an account.</p>
     */
    @Override
    public TransactionResponseDTO deposit(DepositRequestDTO request) {

        // 1. Create transaction aggregate from request
        Transaction tx = transactionMapper.toDepositEntity(request);
        enrichTransaction(tx);

        transactionRepository.save(tx);

        // 2. Create credit ledger entry
        entryService.createDepositEntries(tx, request);

        // 3. Mark transaction as completed
        successTransaction(tx);

        // 4. Audit & publish integration event
        auditService.logCompleted(tx);
        outboxService.publishTransactionCompleted(tx);

        return transactionMapper.toResponse(tx);
    }

    /**
     * Handle WITHDRAW transaction.
     *
     * <p>Money flows OUT of an account.</p>
     */
    @Override
    public TransactionResponseDTO withdraw(WithdrawRequestDTO request) {

        Transaction tx = transactionMapper.toWithdrawEntity(request);
        enrichTransaction(tx);

        transactionRepository.save(tx);

        entryService.createWithdrawEntries(tx, request);

        successTransaction(tx);

        auditService.logCompleted(tx);
        outboxService.publishTransactionCompleted(tx);

        return transactionMapper.toResponse(tx);
    }

    /**
     * Handle TRANSFER transaction.
     *
     * <p>Money flows FROM source account TO destination account.</p>
     */
    @Override
    public TransactionResponseDTO transfer(TransferRequestDTO request) {

        Transaction tx = transactionMapper.toTransferEntity(request);
        enrichTransaction(tx);

        transactionRepository.save(tx);

        entryService.createTransferEntries(tx, request);

        successTransaction(tx);

        auditService.logCompleted(tx);
        outboxService.publishTransactionCompleted(tx);

        return transactionMapper.toResponse(tx);
    }

    /**
     * Enrich transaction aggregate with system-managed fields.
     *
     * <p>This method ensures all transactions share
     * consistent reference generation.</p>
     */
    private void enrichTransaction(Transaction tx) {
        tx.setReferenceCode(generateReference());
    }

    /**
     * Mark transaction as completed.
     *
     * <p>Separated for clarity and reuse across use-cases.</p>
     */
    private void successTransaction(Transaction tx) {
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setCompletedAt(Instant.now());
    }

    /**
     * Generate public transaction reference code.
     *
     * <p>This reference is exposed to end users and external systems
     * and MUST NOT expose internal database identifiers.</p>
     */
    private String generateReference() {
        return "TX-" + UUID.randomUUID();
    }
}