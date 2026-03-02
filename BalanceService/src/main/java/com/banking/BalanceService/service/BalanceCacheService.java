package com.banking.BalanceService.service;

import com.banking.BalanceService.dto.BalanceResponseDTO;
import com.banking.BalanceService.util.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration TTL = Duration.ofMinutes(5);

    public BalanceResponseDTO get(UUID accountId){
        return (BalanceResponseDTO) redisTemplate.opsForValue()
                .get(RedisKeys.balance(accountId));
    }

    public void put(UUID accountId, BalanceResponseDTO balance){
        redisTemplate.opsForValue()
                .set(RedisKeys.balance(accountId), balance, TTL);
    }

    public void evict(UUID accountId){
        redisTemplate.delete(RedisKeys.balance(accountId));
    }
}
