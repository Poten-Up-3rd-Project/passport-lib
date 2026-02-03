package com.lxp.passport.bean.support;

import com.lxp.passport.core.constants.PassportConstants;
import jakarta.servlet.http.HttpServletRequest;

public class PassportExtractor {

    public String extract(HttpServletRequest request) {
        return request.getHeader(PassportConstants.PASSPORT_HEADER_NAME);
    }

}
