package com.lxp.passport.authorization.aspect;

import com.lxp.passport.authorization.annotation.RequireRole;
import com.lxp.passport.authorization.exception.PassportAccessDeniedException;
import com.lxp.passport.core.context.PassportContext;
import com.lxp.passport.core.model.PassportClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class PassportAuthorizationAspect {

    @Around("@annotation(requireRole)")
    public Object authorize(
        ProceedingJoinPoint joinPoint,
        RequireRole requireRole
    ) throws Throwable {
        PassportClaims claims = PassportContext.getOptional().orElse(null);

        if (isNull(claims)) {
            throwUnauthorized();
        }

        Set<String> userRoles = new HashSet<>(claims.roles());
        userRoles.addAll(
            claims.roles().stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .collect(Collectors.toSet())
        );
        userRoles.addAll(
            claims.roles().stream()
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .collect(Collectors.toSet())
        );

        boolean hasAll = Arrays.stream(requireRole.value()).allMatch(userRoles::contains);
        boolean hasAny = requireRole.anyOf().length == 0 ||
            Arrays.stream(requireRole.anyOf()).anyMatch(userRoles::contains);

        if (!hasAll || !hasAny) {
            if (log.isDebugEnabled()) {
                log.debug("Access denied: requiredAll={}, anyOf={}, userRoles={}",
                    Arrays.toString(requireRole.value()), Arrays.toString(requireRole.anyOf()), userRoles);
            }
            throwForbidden();
        }

        return joinPoint.proceed();
    }

    private void throwUnauthorized() {
        throw new PassportAccessDeniedException("UNAUTHORIZED");
    }

    private void throwForbidden() {
        throw new PassportAccessDeniedException("FORBIDDEN");
    }
}
