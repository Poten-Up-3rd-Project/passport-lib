package config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "passport.key")
public class PassportJwtProperties {

    private String secretKey;
    private int durationMillis;

}
