package com.lxp.passport.config;

import com.lxp.passport.filter.PassportFilter;
import com.lxp.passport.support.DefaultPassportHeaderProvider;
import com.lxp.passport.support.PassportExtractor;
import com.lxp.passport.support.PassportHeaderProvider;
import com.lxp.passport.support.PassportVerifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@AutoConfiguration
@EnableConfigurationProperties({PassportJwtProperties.class, PassportFilterProperties.class})
public class PassportBeanAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeyProperties keyProperties(PassportJwtProperties props) {
        return new KeyProperties(props.getSecretKey(), props.getDurationMillis());
    }

    @Bean
    @ConditionalOnMissingBean
    public SecretKey passportSecretKey(KeyProperties keyProperties) {
        return keyProperties.toSecretKey();
    }

    @Bean
    @ConditionalOnMissingBean
    public PassportExtractor passportExtractor() {
        return new PassportExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public PassportVerifier passportVerifier(KeyProperties keyProperties) {
        return new PassportVerifier(keyProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public PassportHeaderProvider passportHeaderProvider() {
        return new DefaultPassportHeaderProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public PassportFilter passportFilter(PassportExtractor ex, PassportVerifier vf, PassportFilterProperties pp) {
        return new PassportFilter(ex, vf, pp);
    }
}
