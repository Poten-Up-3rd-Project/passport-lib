package config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@EnableConfigurationProperties(PassportJwtProperties.class)
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
}
