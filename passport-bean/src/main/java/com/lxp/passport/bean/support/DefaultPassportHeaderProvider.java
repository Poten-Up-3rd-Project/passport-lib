package com.lxp.passport.bean.support;

import com.lxp.passport.core.constants.PassportConstants;
import com.lxp.passport.core.context.PassportContext;
import com.lxp.passport.core.model.PassportClaims;
import com.lxp.passport.core.support.PassportHeaderProvider;

import java.util.Map;

import static java.util.Objects.isNull;

public class DefaultPassportHeaderProvider implements PassportHeaderProvider {

    @Override
    public Map<String, String> headers() {
        PassportClaims claims = PassportContext.get();

        if (isClaimsNull(claims)) {
            return Map.of();
        }

        return Map.of(PassportConstants.PASSPORT_HEADER_NAME, claims.rawToken());
    }

    private boolean isClaimsNull(PassportClaims claims) {
        return isNull(claims) || isNull(claims.rawToken());
    }
}
