package com.banking.TransactionService.service;

import com.banking.TransactionService.dto.request.DepositRequestDTO;
import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.request.WithdrawRequestDTO;
import com.banking.TransactionService.dto.response.TransactionResponseDTO;

public interface TransactionService {

    TransactionResponseDTO deposit(DepositRequestDTO request);

    TransactionResponseDTO withdraw(WithdrawRequestDTO request);

    TransactionResponseDTO transfer(TransferRequestDTO request);

}
