package com.lxp.passport.bean.exception;

import com.lxp.passport.core.exception.PassportErrorCode;
import com.lxp.passport.core.exception.PassportException;

public class UnauthorizedException extends PassportException {

    public UnauthorizedException(String message) {
        super(PassportErrorCode.UNAUTHORIZED_ACCESS, message);
    }
}
