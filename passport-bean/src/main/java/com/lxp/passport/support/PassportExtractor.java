package com.lxp.passport.support;

import com.lxp.passport.constants.PassportConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class PassportExtractor {

    public String extract(HttpServletRequest request) {
        return request.getHeader(PassportConstants.PASSPORT_HEADER_NAME);
    }

}
