package com.lxp.passport.core.context;

import com.lxp.passport.core.model.PassportClaims;

import java.util.Optional;

/**
 * 아래와 같이 사용할 것
 *
 * <code>
 * try (PassportContext ignored = PassportContext.open(claims)) {
 *     filterChain.doFilter(request, response);
 * }
 * </code>
 */
public class PassportContext implements AutoCloseable {

    private static final ThreadLocal<PassportClaims> HOLDER = new ThreadLocal<>();

    private PassportContext() {
    }

    public static PassportContext open(PassportClaims claims) {
        HOLDER.set(claims);
        return new PassportContext();
    }

    public static PassportClaims get() {
        return HOLDER.get();
    }

    public static Optional<PassportClaims> getOptional() {
        return Optional.ofNullable(HOLDER.get());
    }

    public static String getCurrentToken() {
        PassportClaims claims = HOLDER.get();
        return claims != null ? claims.rawToken() : null;
    }

    public static void clear() {
        HOLDER.remove();
    }

    @Override
    public void close() {
        clear();
    }
}
