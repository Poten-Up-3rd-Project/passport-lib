package com.lxp.passport.bean.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
@ConfigurationProperties(prefix = "passport.filter")
public class PassportFilterProperties {

    private final List<String> excludePaths;

    public PassportFilterProperties(List<String> excludePaths) {
        this.excludePaths = isNull(excludePaths) ? Collections.emptyList() : excludePaths;
    }
}
