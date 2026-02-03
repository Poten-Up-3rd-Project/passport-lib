package com.lxp.passport.authorization.config;

import com.lxp.passport.authorization.aspect.PassportAuthorizationAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
@ConditionalOnMissingClass("org.springframework.security.core.context.SecurityContextHolder")
public class PassportAuthorizationAutoConfiguration {

    @Bean
    public PassportAuthorizationAspect passportAuthorizationAspect() {
        return new PassportAuthorizationAspect();
    }
}
