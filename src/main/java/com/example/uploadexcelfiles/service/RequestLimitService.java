package com.example.uploadexcelfiles.service;

import com.example.uploadexcelfiles.exception.RateLimitExceededException;
import org.springframework.stereotype.Service;
import com.google.common.util.concurrent.RateLimiter;

@Service
public class RequestLimitService {
    private static final double REQUESTS_PER_MINUTE = 100;
    private static final double REQUESTS_PER_DAY = 10000;

    private final RateLimiter minuteRateLimiter;
    private final RateLimiter dayRateLimiter;

    public RequestLimitService() {
        minuteRateLimiter = RateLimiter.create(REQUESTS_PER_MINUTE / 60.0);
        dayRateLimiter = RateLimiter.create(REQUESTS_PER_DAY / (24.0 * 60.0 * 60.0));
    }

    public void checkRateLimit() {
        boolean canMakeRequest = minuteRateLimiter.tryAcquire();
        if (!canMakeRequest) {
            throw new RateLimitExceededException("Превышено ограничение количества запросов в минуту");
        }

        canMakeRequest = dayRateLimiter.tryAcquire();
        if (!canMakeRequest) {
            throw new RateLimitExceededException("Превышено ограничение количества запросов в день");
        }
    }
}
