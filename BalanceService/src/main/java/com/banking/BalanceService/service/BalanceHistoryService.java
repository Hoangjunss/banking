package com.banking.BalanceService.service;

import com.banking.BalanceService.dto.BalanceHistoryResponseDTO;
import com.banking.BalanceService.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BalanceHistoryService {

    // 1. Lấy lịch sử phân trang (Quan trọng vì lịch sử có thể lên tới hàng triệu dòng)
    Page<BalanceHistoryResponseDTO> getHistoryPaged(UUID accountId, Pageable pageable);

    // 2. Lọc lịch sử theo khoảng thời gian (Dùng cho tính năng sao kê tháng/năm)
    List<BalanceHistoryResponseDTO> getHistoryByDateRange(
            UUID accountId,
            LocalDateTime startDate,
            LocalDateTime endDate);

    // 3. Lọc theo loại giao dịch (Chỉ xem tiền vào hoặc chỉ xem tiền ra)
    List<BalanceHistoryResponseDTO> getHistoryByType(UUID accountId, TransactionType type);

    // 4. Tìm kiếm giao dịch cụ thể theo mã TransactionID (Đối soát khi khách hàng khiếu nại)
    BalanceHistoryResponseDTO getHistoryByTransactionId(UUID transactionId);

    // 5. Xuất báo cáo tổng hợp (Ví dụ: Tổng thu/Tổng chi trong tháng)
    // Giúp khách hàng quản lý tài chính cá nhân
    void generateMonthlyReport(UUID accountId, int month, int year);

    // 6. Kiểm tra tính toàn vẹn của số dư (Audit)
    // Tính toán lại từ lịch sử xem có khớp với số dư hiện tại trong bảng Balance không
    boolean verifyBalanceIntegrity(UUID accountId);
}