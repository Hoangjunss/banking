package com.banking.TransactionService.service;

import com.banking.TransactionService.dto.request.DepositRequestDTO;
import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.request.WithdrawRequestDTO;
import com.banking.TransactionService.entity.Transaction;

public interface TransactionEntryService {
    void createDepositEntries(Transaction transaction, DepositRequestDTO request);

    void createWithdrawEntries(Transaction transaction, WithdrawRequestDTO request);

    void createTransferEntries(Transaction transaction, TransferRequestDTO request);
}
