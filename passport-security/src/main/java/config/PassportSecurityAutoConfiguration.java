package config;

import filter.PassportAuthenticationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ConditionalOnClass(SecurityContextHolder.class)
public class PassportSecurityAutoConfiguration {

    @Bean
    public PassportAuthenticationFilter passportAuthenticationFilter() {
        return new PassportAuthenticationFilter();
    }

}
