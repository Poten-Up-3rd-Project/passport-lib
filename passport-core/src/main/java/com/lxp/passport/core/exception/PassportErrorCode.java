package com.lxp.passport.core.exception;

import com.lxp.common.domain.exception.ErrorCode;

public enum PassportErrorCode implements ErrorCode {

    MISSING_REQUIRED_FIELD("BAD_REQUEST", "PASSPORT_001", "필수 입력 항목이 누락되었습니다."),
    UNAUTHORIZED_ACCESS("UNAUTHORIZED", "PASSPORT_002", "유효하지 않거나 만료된 토큰입니다."),
    FORBIDDEN("FORBIDDEN", "PASSPORT_003", "해당 리소스에 접근할 권한이 없습니다."),
    ;

    private final String code;
    private final String message;
    private final String group;

    PassportErrorCode(String code, String message, String group) {
        this.code = code;
        this.message = message;
        this.group = group;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }

}
