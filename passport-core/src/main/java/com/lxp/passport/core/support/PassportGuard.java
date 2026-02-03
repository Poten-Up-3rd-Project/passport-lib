package com.lxp.passport.core.support;

import com.lxp.common.domain.exception.DomainException;
import com.lxp.passport.core.exception.PassportErrorCode;
import com.lxp.passport.core.exception.PassportException;

import java.util.Objects;

public class PassportGuard {

    public static <T> T requireNonNull(T obj, String message) {
        if (Objects.isNull(obj)) {
            throw missing(message);
        }
        return obj;
    }

    public static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw missing(message);
        }
        return value;
    }

    private static DomainException missing(String message) {
        return new PassportException(PassportErrorCode.MISSING_REQUIRED_FIELD, message);
    }
}
