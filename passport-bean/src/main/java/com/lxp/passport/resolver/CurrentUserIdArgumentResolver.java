package com.lxp.passport.resolver;

import com.lxp.passport.annotation.CurrentUserId;
import com.lxp.passport.context.PassportContext;
import com.lxp.passport.model.PassportClaims;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static java.util.Objects.nonNull;

public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class)
            && parameter.getParameterType().equals(String.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) throws Exception {
        PassportClaims claims = PassportContext.getOptional().orElse(null);

        if (nonNull(claims)) {
            return convert(parameter, claims.userId());
        }

        CurrentUserId annotation = parameter.getParameterAnnotation(CurrentUserId.class);

        if (nonNull(annotation) && annotation.required()) {
            throw new IllegalStateException("Current user is not authenticated");
        }

        return null;
    }

    private Object convert(MethodParameter parameter, Object value) {
        if (parameter.getParameterType().equals(Long.class)) {
            return Long.valueOf(value.toString());
        }
        return value.toString();
    }
}
