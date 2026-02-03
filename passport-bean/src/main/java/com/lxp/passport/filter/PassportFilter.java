package com.lxp.passport.filter;

import com.lxp.passport.context.PassportContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.lxp.passport.model.PassportClaims;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.lxp.passport.support.PassportExtractor;
import com.lxp.passport.support.PassportVerifier;

import java.io.IOException;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class PassportFilter extends OncePerRequestFilter {

    private final PassportExtractor extractor;
    private final PassportVerifier verifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String encodedPassport = extractor.extract(request);

        if (isNull(encodedPassport)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            PassportClaims claims = verifier.verify(encodedPassport);

            try (PassportContext ignored = PassportContext.open(claims)) {
                MDC.put("traceId", claims.traceId());
                filterChain.doFilter(request, response);
            } finally {
                MDC.clear();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
