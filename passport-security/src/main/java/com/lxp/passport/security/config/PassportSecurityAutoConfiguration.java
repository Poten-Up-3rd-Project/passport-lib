package com.lxp.passport.security.config;

import com.lxp.passport.security.filter.PassportAuthenticationEntryPoint;
import com.lxp.passport.security.filter.PassportAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public PassportAuthenticationEntryPoint passportAuthenticationEntryPoint() {
        return new PassportAuthenticationEntryPoint();
    }

}
