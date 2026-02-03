package com.lxp.passport.support;

import com.lxp.passport.constants.PassportConstants;
import com.lxp.passport.context.PassportContext;
import com.lxp.passport.model.PassportClaims;

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
