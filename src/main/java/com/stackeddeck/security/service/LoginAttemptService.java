package com.stackeddeck.security.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
public class LoginAttemptService {
    private final Cache<String, Integer> cache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build();
    private static final int MAX = 10;

    public void onSuccess(String key) { cache.invalidate(key);}
    public void onFailure(String key) {
        var n = cache.asMap().merge(key, 1, Integer::sum);
        cache.put(key,n);
    }

    public boolean blocked(String key) {
        var n = cache.getIfPresent(key);
        return n != null && n >=MAX;
    }
}
