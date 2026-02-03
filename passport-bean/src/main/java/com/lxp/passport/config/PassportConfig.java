package com.lxp.passport.config;

import com.lxp.passport.support.PassportEncoder;
import com.lxp.passport.support.PassportVerifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class PassportConfig {

    @Bean
    @ConditionalOnMissingBean
    public SecretKey passportSecretKey(KeyProperties properties) {
        return properties.toSecretKey();
    }

    @Bean
    @ConditionalOnMissingBean
    public PassportVerifier passportVerifier(KeyProperties properties) {
        return new PassportVerifier(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public PassportEncoder passportEncoder(KeyProperties properties) {
        return new PassportEncoder(properties);
    }

}
