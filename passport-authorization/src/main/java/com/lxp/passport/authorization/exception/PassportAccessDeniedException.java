package com.lxp.passport.authorization.exception;

import com.lxp.passport.core.exception.PassportErrorCode;
import com.lxp.passport.core.exception.PassportException;

public class PassportAccessDeniedException extends PassportException {
    public PassportAccessDeniedException(String message) {
        super(PassportErrorCode.UNAUTHORIZED_ACCESS, message);
    }
}
