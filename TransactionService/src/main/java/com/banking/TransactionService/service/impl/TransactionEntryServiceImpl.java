package com.banking.TransactionService.service.impl;

import com.banking.TransactionService.dto.request.DepositRequestDTO;
import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.request.WithdrawRequestDTO;
import com.banking.TransactionService.entity.EntryType;
import com.banking.TransactionService.entity.Transaction;
import com.banking.TransactionService.entity.TransactionEntry;
import com.banking.TransactionService.mapper.TransactionEntryMapper;
import com.banking.TransactionService.repository.TransactionEntryRepository;
import com.banking.TransactionService.service.TransactionEntryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class TransactionEntryServiceImpl implements TransactionEntryService {

    private final TransactionEntryRepository entryRepository;
    private final TransactionEntryMapper entryMapper;

    public TransactionEntryServiceImpl(
            TransactionEntryRepository entryRepository,
            TransactionEntryMapper entryMapper
    ) {
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
    }

    /**
     * DEPOSIT:
     * - CREDIT target account
     */
    @Override
    public void createDepositEntries(Transaction transaction, DepositRequestDTO request) {

        TransactionEntry entry = entryMapper.toEntry(
                transaction,
                EntryType.CREDIT
        );

        entry.setAccountId(request.getToAccountId());
        entry.setAmount(transaction.getAmount());

        entryRepository.save(entry);
    }

    /**
     * WITHDRAW:
     * - DEBIT source account
     */
    @Override
    public void createWithdrawEntries(Transaction transaction, WithdrawRequestDTO request) {

        TransactionEntry entry = entryMapper.toEntry(
                transaction,
                EntryType.DEBIT
        );

        entry.setAccountId(request.getFromAccountId());
        entry.setAmount(transaction.getAmount());

        entryRepository.save(entry);
    }

    /**
     * TRANSFER:
     * - DEBIT source account
     * - CREDIT destination account
     */
    @Override
    public void createTransferEntries(Transaction transaction, TransferRequestDTO request) {

        // Debit
        TransactionEntry debitEntry = entryMapper.toEntry(
                transaction,
                EntryType.DEBIT
        );
        debitEntry.setAccountId(request.getFromAccountId());
        debitEntry.setAmount(transaction.getAmount());

        // Credit
        TransactionEntry creditEntry = entryMapper.toEntry(
                transaction,
                EntryType.CREDIT
        );
        creditEntry.setAccountId(request.getToAccountId());
        creditEntry.setAmount(transaction.getAmount());

        entryRepository.save(debitEntry);
        entryRepository.save(creditEntry);
    }
}
