package com.lxp.passport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "passport.filter")
public record PassportFilterProperties(List<String> excludePaths) {
}
