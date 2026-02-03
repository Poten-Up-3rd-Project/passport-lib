package com.lxp.passport.security.filter;

import com.lxp.passport.security.adapter.PassportSecurityAdapter;
import com.lxp.passport.core.context.PassportContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.lxp.passport.core.model.PassportClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class PassportAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        PassportClaims claims = PassportContext.get();

        if (nonNull(claims) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
            Authentication auth = PassportSecurityAdapter.toAuthentication(claims);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
