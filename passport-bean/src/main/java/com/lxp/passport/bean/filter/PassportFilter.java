package com.lxp.passport.bean.filter;

import com.lxp.passport.bean.config.PassportFilterProperties;
import com.lxp.passport.bean.support.PassportExtractor;
import com.lxp.passport.core.context.PassportContext;
import com.lxp.passport.core.exception.InvalidPassportException;
import com.lxp.passport.core.model.PassportClaims;
import com.lxp.passport.core.support.PassportVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
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

        final String uri = request.getRequestURI();
        final String encodedPassport = extractor.extract(request);
        final boolean hasHeader = !isNull(encodedPassport);
        log.debug("PassportFilter IN: uri={}, hasPassportHeader={}", uri, hasHeader);

        if (!hasHeader) {
            log.debug("PassportFilter SKIP: no X-Passport header for uri={}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        PassportClaims claims;
        try {
            claims = verifier.verify(encodedPassport);
        } catch (InvalidPassportException e) {
            log.warn("Passport verification failed for uri={} : {} ({})",
                uri, e.getMessage(), e.getClass().getSimpleName());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.debug("Passport verified: uri={}, userId={}, roles={}, traceId={}",
            uri, claims.userId(), claims.roles(), claims.traceId());

        try (PassportContext ignored = PassportContext.open(claims)) {
            MDC.put("traceId", claims.traceId());
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        List<String> excludes = properties.getExcludePaths();
        if (excludes == null || excludes.isEmpty()) {
            log.debug("No exclude paths configured; filtering applied for uri={}", path);
            return false;
        }

        Optional<String> matched = excludes.stream()
            .filter(pattern -> pathMatcher.match(pattern, path))
            .findFirst();

        boolean skip = matched.isPresent();
        log.debug("Exclude check: uri={}, skip={}, matchedPattern={}", path, skip, matched.orElse("<none>"));
        return skip;
    }
}
