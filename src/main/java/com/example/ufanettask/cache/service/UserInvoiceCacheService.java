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
    private static final String KEY_PREFIX = "userId:";

    private final Cache<Long, UserInvoice> localUserInvoiceCache;

    public void saveInvoiceToUser(Invoice invoice) {
        try {
            Long userId = invoice.getUserId();
            String key = KEY_PREFIX + userId;

            UserInvoice userInvoice = getUserInvoice(userId);
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

    public UserInvoice getUserInvoice(Long userId) {
        UserInvoice userInvoice;
        try {
            userInvoice = findUserInvoiceById(userId).orElse(new UserInvoice(userId));
        } catch (Exception e) {
            userInvoice = localUserInvoiceCache.getIfPresent(userId);
            if (userInvoice == null) {
                userInvoice = new UserInvoice(userId);
            }
        }
        localUserInvoiceCache.put(userId, userInvoice);
        return userInvoice;
    }

    public Optional<UserInvoice> findUserInvoiceById(Long userId) {
        String key = KEY_PREFIX + userId;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
