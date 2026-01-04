package com.banking.TransactionService.controller;



import com.banking.TransactionService.dto.ApiResponse;
import com.banking.TransactionService.dto.request.DepositRequestDTO;
import com.banking.TransactionService.dto.request.TransferRequestDTO;
import com.banking.TransactionService.dto.request.WithdrawRequestDTO;
import com.banking.TransactionService.dto.response.TransactionResponseDTO;
import com.banking.TransactionService.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for transaction operations.
 *
 * Responsibilities:
 * - receive HTTP requests
 * - validate input
 * - delegate to TransactionService
 * - wrap response into ApiResponse
 *
 * MUST NOT contain business logic.
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Deposit money into an account.
     */
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> deposit(
           @RequestBody DepositRequestDTO request
    ) {
        TransactionResponseDTO response = transactionService.deposit(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Withdraw money from an account.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> withdraw(
             @RequestBody WithdrawRequestDTO request
    ) {
        TransactionResponseDTO response = transactionService.withdraw(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Transfer money between accounts.
     */
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> transfer(
             @RequestBody TransferRequestDTO request
    ) {
        TransactionResponseDTO response = transactionService.transfer(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}