package com.lxp.passport.adapter;

import com.lxp.passport.model.PassportClaims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class PassportSecurityAdapter {

    public static Authentication toAuthentication(PassportClaims claims) {
        return new UsernamePasswordAuthenticationToken(
            claims.userId(),
            claims.rawToken(),
            claims.roles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList()
        );
    }
}
