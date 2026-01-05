package com.banking.AccountService.controller;

import com.banking.AccountService.dto.ApiResponse;
import com.banking.AccountService.dto.request.*;
import com.banking.AccountService.dto.response.AccountResponseDTO;
import com.banking.AccountService.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Validated // Kích hoạt validation cho các PathVariable
public class AccountController {

    private final AccountService accountService;

    // 1. Mở tài khoản mới
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponseDTO>> openAccount(
             @RequestBody OpenAccountRequestDTO request) {
        AccountResponseDTO response = accountService.openAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 2. Cập nhật thông tin tài khoản
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponseDTO>> updateInfo(
            @PathVariable UUID id,
            @RequestBody UpdateAccountInfoRequestDTO request) {
        AccountResponseDTO response = accountService.updateInfo(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 3. Khóa tài khoản (Freeze)
    @PatchMapping("/{id}/freeze")
    public ResponseEntity<ApiResponse<Void>> freezeAccount(
            @PathVariable UUID id,
            @Valid @RequestBody FreezeAccountRequestDTO request) {
        accountService.freezeAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    // 4. Mở khóa tài khoản (Unfreeze)
    @PatchMapping("/{id}/unfreeze")
    public ResponseEntity<ApiResponse<Void>> unfreezeAccount(
            @PathVariable UUID id,
            @RequestBody UnfreezeAccountRequestDTO request) {
        accountService.unfreezeAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    // 5. Thiết lập hạn mức (Limits)
    @PatchMapping("/{id}/limits")
    public ResponseEntity<ApiResponse<Void>> setLimit(
            @PathVariable UUID id,
            @RequestBody SetAccountLimitRequestDTO request) {
        accountService.setLimit(id, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    // 6. Đóng tài khoản (Close)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> closeAccount(
            @PathVariable UUID id,
             @RequestBody CloseAccountRequestDTO request) {
        accountService.closeAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
