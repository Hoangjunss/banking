package com.banking.AccountService.mapper;



import com.banking.AccountService.dto.response.AccountResponseDTO;
import com.banking.AccountService.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // Nếu các tên trường (field name) giống hệt nhau, bạn không cần dùng @Mapping
    // MapStruct sẽ tự động map: id, accountNumber, ownerId, openedAt, closedAt
    // Đối với Enum -> String, nó cũng tự xử lý mặc định.
    AccountResponseDTO toResponseDTO(Account account);

    // Ngược lại nếu bạn cần tạo Entity từ DTO (thường dùng cho Create/Update)
    @Mapping(target = "version", ignore = true) // Luôn ignore version khi map từ DTO
    Account toEntity(AccountResponseDTO dto);
}