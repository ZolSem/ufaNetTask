package com.example.ufanettask.cache.configuration;

import com.example.ufanettask.cache.entity.UserInvoice;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.cache.local")
@Slf4j
public class LocalCacheConfig {

    private boolean enabled = true;
    private long maxSize = 1000;
    private Duration expireAfterWrite = Duration.ofMinutes(10);
    private Duration expireAfterAccess = Duration.ofMinutes(5);

    @Bean
    public Cache<Long, UserInvoice> userInvoiceCache() {
        if (!enabled) {
            log.info("Local cache is disabled");
            return null;
        }
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireAfterWrite);
        Cache<Long, UserInvoice> cache = builder.build();
        return cache;
    }
}
