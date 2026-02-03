package com.lxp.passport.bean.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "passport.filter")
public class PassportFilterProperties {

    private List<String> excludePaths;

}
