package com.lxp.passport.exception;

import com.lxp.common.domain.exception.DomainException;

public class PassportException extends DomainException {

    public PassportException(PassportErrorCode errorCode) {
        super(errorCode);
    }

    public PassportException(PassportErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
