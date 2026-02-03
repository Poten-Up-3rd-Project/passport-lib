package com.lxp.passport.authorization.aspect;

import com.lxp.passport.authorization.annotation.RequireRole;
import com.lxp.passport.authorization.exception.PassportAccessDeniedException;
import com.lxp.passport.core.context.PassportContext;
import com.lxp.passport.core.model.PassportClaims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

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

        boolean hasAll = Arrays.stream(requireRole.value()).allMatch(userRoles::contains);

        boolean hasAny = requireRole.anyOf().length == 0 ||
            Arrays.stream(requireRole.anyOf()).anyMatch(userRoles::contains);

        if (!hasAll || !hasAny) {
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
