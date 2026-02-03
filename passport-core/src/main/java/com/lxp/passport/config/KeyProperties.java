package com.lxp.passport.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import com.lxp.passport.support.PassportGuard;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class KeyProperties {

    private final String secretKey;
    @Getter
    private final int durationMillis;

    public KeyProperties(String secretKey, int durationMillis) {
        this.secretKey = secretKey;
        this.durationMillis = durationMillis;
    }

    public SecretKey toSecretKey() {
        PassportGuard.requireNonBlank(secretKey, "jwt secret key cannot be null or empty");
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
