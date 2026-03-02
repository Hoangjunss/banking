package com.banking.BalanceService.service;

import com.banking.BalanceService.dto.BalanceResponseDTO;
import com.banking.BalanceService.dto.CreditRequestDTO;
import com.banking.BalanceService.dto.DebitRequestDTO;
import com.banking.BalanceService.entity.Balance;
import com.banking.BalanceService.entity.BalanceHistory;
import com.banking.BalanceService.entity.TransactionType;
import com.banking.BalanceService.mapper.BalanceMapper;
import com.banking.BalanceService.repository.BalanceHistoryRepository;
import com.banking.BalanceService.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository historyRepository;
    private final BalanceMapper balanceMapper;

    @Override
    public BalanceResponseDTO getBalance(UUID accountId) {
        Balance balance = balanceRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        return balanceMapper.toResponseDTO(balance);
    }

    @Override
    @Transactional
    public void credit(CreditRequestDTO request) {
        // Kiểm tra Idempotency (Chống xử lý trùng)
        if (historyRepository.existsByTransactionId(request.getTransactionId())) {
            log.warn("TransactionId {} đã được xử lý", request.getTransactionId());
            return;
        }

        Balance balance = balanceRepository.findByAccountId(request.getAccountId())
                .orElseGet(() -> createInitialBalance(request.getAccountId()));

        BigDecimal oldBalance = balance.getAvailableBalance();
        balance.setAvailableBalance(oldBalance.add(request.getAmount()));
        balanceRepository.save(balance);

        saveHistory(request.getAccountId(), request.getTransactionId(),
                request.getAmount(), oldBalance, balance.getAvailableBalance(), TransactionType.CREDIT);
    }

    @Override
    @Transactional
    public void debit(DebitRequestDTO request) {
        if (historyRepository.existsByTransactionId(request.getTransactionId())) return;

        Balance balance = balanceRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (balance.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Số dư không đủ");
        }

        BigDecimal oldBalance = balance.getAvailableBalance();
        balance.setAvailableBalance(oldBalance.subtract(request.getAmount()));
        balanceRepository.save(balance);

        saveHistory(request.getAccountId(), request.getTransactionId(),
                request.getAmount(), oldBalance, balance.getAvailableBalance(), TransactionType.DEBIT);
    }

    private Balance createInitialBalance(UUID accountId) {
        Balance balance = new Balance();
        balance.setAccountId(accountId);
        balance.setAvailableBalance(BigDecimal.ZERO);
        balance.setBlockedBalance(BigDecimal.ZERO);
        return balance;
    }

    private void saveHistory(UUID accountId, UUID txId, BigDecimal amount,
                             BigDecimal before, BigDecimal after, TransactionType type) {
        BalanceHistory history = new BalanceHistory();
        history.setAccountId(accountId);
        history.setTransactionId(txId);
        history.setAmount(amount);
        history.setBalanceBefore(before);
        history.setBalanceAfter(after);
        history.setType(type);
        historyRepository.save(history);
    }
}