package com.lxp.passport.core.model;

import java.util.List;

public record PassportClaims(
    String userId,
    List<String> roles,
    String traceId,
    String rawToken
) {
}
