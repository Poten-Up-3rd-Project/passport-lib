package com.lxp.passport.filter;

import com.lxp.passport.config.PassportFilterProperties;
import com.lxp.passport.context.PassportContext;
import com.lxp.passport.model.PassportClaims;
import com.lxp.passport.support.PassportExtractor;
import com.lxp.passport.support.PassportVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class PassportFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final PassportExtractor extractor;
    private final PassportVerifier verifier;
    private final PassportFilterProperties properties;

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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return properties.excludePaths().stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
