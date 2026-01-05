package com.example.mfa.service;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.RandomUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.ott.DefaultOneTimeToken;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * 自定义OTT令牌生成服务。
 * Spring Security提供两种实现：InMemoryOneTimeTokenService、JdbcOneTimeTokenService
 */
public class CustomOneTimePasswordService implements OneTimeTokenService {
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int OTT_LENGTH = 6;
    private final Cache<String, OneTimeToken> cache = CacheUtil.newTimedCache(OTP_EXPIRY_MINUTES * 60 * 1000);
    private final Duration expiresIn = Duration.ofMinutes(OTP_EXPIRY_MINUTES);
    private final Clock clock = Clock.systemDefaultZone();

    @NonNull
    @Override
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        // 生成6位数字OTP
        String token = RandomUtil.randomString(OTT_LENGTH);
        Instant expireAt = clock.instant().plus(expiresIn);
        OneTimeToken oneTimeToken = new DefaultOneTimeToken(
                token,
                request.getUsername(),
                expireAt
        );
        cache.put(token, oneTimeToken);
        return oneTimeToken;
    }

    /**
     * 验证OTP
     */
    @Override
    public @Nullable OneTimeToken consume(@NonNull OneTimeTokenAuthenticationToken authenticationToken) {
        String tokenValue = authenticationToken.getTokenValue();
        return cache.get(tokenValue);
    }
}