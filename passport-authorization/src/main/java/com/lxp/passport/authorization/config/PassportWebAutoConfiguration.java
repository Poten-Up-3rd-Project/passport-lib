package com.lxp.passport.authorization.config;

import com.lxp.passport.authorization.resolver.CurrentUserIdArgumentResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;


@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
public class PassportWebAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingClass("org.springframework.security.core.context.SecurityContextHolder")
    public CurrentUserIdArgumentResolver currentUserIdArgumentResolver() {
        return new CurrentUserIdArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserIdArgumentResolver());
    }
}
