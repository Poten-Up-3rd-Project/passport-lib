package com.lxp.passport.bean.config;

import com.lxp.passport.bean.filter.PassportFilter;
import com.lxp.passport.bean.support.DefaultPassportHeaderProvider;
import com.lxp.passport.bean.support.PassportExtractor;
import com.lxp.passport.core.config.KeyProperties;
import com.lxp.passport.core.support.PassportHeaderProvider;
import com.lxp.passport.core.support.PassportVerifier;
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
