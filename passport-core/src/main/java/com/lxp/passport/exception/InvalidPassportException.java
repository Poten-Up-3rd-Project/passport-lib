package com.lxp.passport.exception;

import com.lxp.common.domain.exception.DomainException;

public class InvalidPassportException extends DomainException {

    public InvalidPassportException(String message) {
        super(PassportErrorCode.UNAUTHORIZED_ACCESS, message);
    }
}
