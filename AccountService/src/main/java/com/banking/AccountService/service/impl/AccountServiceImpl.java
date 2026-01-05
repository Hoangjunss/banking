package com.banking.AccountService.service.impl;

import com.banking.AccountService.dto.request.*;
import com.banking.AccountService.dto.response.AccountResponseDTO;
import com.banking.AccountService.entity.Account;
import com.banking.AccountService.entity.AccountStatus;
import com.banking.AccountService.mapper.AccountMapper;
import com.banking.AccountService.respository.AccountRepository;
import com.banking.AccountService.service.AccountEventService;
import com.banking.AccountService.service.AccountLimitService;
import com.banking.AccountService.service.AccountOutboxEventService;
import com.banking.AccountService.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    // Gọi các Service con thay vì gọi Repository trực tiếp
    private final AccountOutboxEventService outboxService;
    private final AccountLimitService limitService;
    private final AccountEventService eventService;

    @Override
    public AccountResponseDTO openAccount(OpenAccountRequestDTO request) {
        // 1. Lưu Account
        Account account = accountMapper.toEntity(request);
        account.setStatus(AccountStatus.ACTIVE);
        Account savedAccount = accountRepository.save(account);

        // 2. Gọi Limit Service thiết lập hạn mức
        limitService.setDefaultLimits(savedAccount);

        // 3. Gọi Event Service ghi lại lịch sử mở tài khoản
        eventService.logEvent(savedAccount.getId(), "OPEN_ACCOUNT", "System initialized");

        // 4. Gọi Outbox Service để đồng bộ sang Balance Service
        outboxService.createEvent(savedAccount.getId(), "ACCOUNT_CREATED", savedAccount);

        return accountMapper.toResponseDTO(savedAccount);
    }

    @Override
    public AccountResponseDTO updateInfo(UUID id, UpdateAccountInfoRequestDTO request) {
        Account account = getAccountOrThrow(id);

        // Cập nhật thông tin cơ bản
        account.setDisplayName(request.getDisplayName()); // Giả định bạn có field này
        // ... các field khác từ request

        Account updatedAccount = accountRepository.save(account);
        eventService.logEvent(id, "UPDATE_INFO", "Customer updated profile information");

        return accountMapper.toResponseDTO(updatedAccount);
    }

    @Override
    public void freezeAccount(UUID id, FreezeAccountRequestDTO request) {
        Account account = getAccountOrThrow(id);
        account.setStatus(AccountStatus.LOCKED); // Hoặc FROZEN tùy Enum của bạn

        accountRepository.save(account);

        eventService.logEvent(id, "FREEZE_ACCOUNT", "Reason: " + request.getReason());
        outboxService.createEvent(id, "ACCOUNT_FROZEN", request);
    }

    @Override
    public void unfreezeAccount(UUID id, UnfreezeAccountRequestDTO request) {
        Account account = getAccountOrThrow(id);
        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        eventService.logEvent(id, "UNFREEZE_ACCOUNT", "Account reactivated");
        outboxService.createEvent(id, "ACCOUNT_UNFROZEN", null);
    }

    @Override
    public void setLimit(UUID id, SetAccountLimitRequestDTO request) {
        Account account = getAccountOrThrow(id);

        // Gọi LimitService để xử lý logic cập nhật hạn mức
        limitService.updateLimit(account, request);

        eventService.logEvent(id, "SET_LIMIT", "New limit set for type: " + request.getLimitType());
        outboxService.createEvent(id, "LIMIT_CHANGED", request);
    }

    @Override
    public void closeAccount(UUID id, CloseAccountRequestDTO request) {
        Account account = getAccountOrThrow(id);
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());

        accountRepository.save(account);

        eventService.logEvent(id, "CLOSE_ACCOUNT", "Reason: " + request.getReason());
        outboxService.createEvent(id, "ACCOUNT_CLOSED", null);
    }

    // Hàm dùng chung để tránh lặp code
    private Account getAccountOrThrow(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
    }
}