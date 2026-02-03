package config;

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

}
