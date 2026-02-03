package com.lxp.passport.config;

import com.lxp.passport.filter.PassportAuthenticationEntryPoint;
import com.lxp.passport.filter.PassportAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

@AutoConfiguration
@ConditionalOnClass(SecurityContextHolder.class)
public class PassportSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PassportAuthenticationFilter passportAuthenticationFilter() {
        return new PassportAuthenticationFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint passportAuthenticationEntryPoint() {
        return new PassportAuthenticationEntryPoint();
    }

}
