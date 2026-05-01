package com.example.ufanettask.cache.service;

import com.example.ufanettask.cache.entity.Invoice;
import com.example.ufanettask.cache.entity.UserInvoice;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInvoiceCacheService {

    private final RedisTemplate<String, UserInvoice> redisTemplate;
    private final Cache<Long, UserInvoice> localUserInvoiceCache;

    private static final String KEY_PREFIX = "userId:";

    public void saveInvoiceToUser(Invoice invoice) {
        try {
            Long userId = invoice.getUserId();
            String key = KEY_PREFIX + userId;

            UserInvoice userInvoice;
            try {
                userInvoice = findUserInvoiceById(userId).orElse(new UserInvoice(userId));
            } catch (Exception e) {
                userInvoice = localUserInvoiceCache.getIfPresent(userId);
                if (userInvoice == null) {
                    userInvoice = new UserInvoice(userId);
                }
            }

            if (userInvoice.getActivationDate() == null) {
                userInvoice.setActivationDate(invoice.getActivationDate());
            }
            userInvoice.addInvoice(invoice);
            localUserInvoiceCache.put(userId, userInvoice);

            redisTemplate.opsForValue().set(key, userInvoice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<UserInvoice> findUserInvoiceById(Long userId) {
        String key = KEY_PREFIX + userId;
        UserInvoice userInvoice;
        try {
            userInvoice = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            userInvoice = localUserInvoiceCache.getIfPresent(userId);
        }
        if (userInvoice != null) {
            localUserInvoiceCache.put(userId, userInvoice);
        }
        return Optional.ofNullable(userInvoice);
    }
}
