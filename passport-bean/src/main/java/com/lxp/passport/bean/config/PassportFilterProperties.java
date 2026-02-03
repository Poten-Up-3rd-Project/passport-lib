package com.lxp.passport.bean.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "passport.filter")
public record PassportFilterProperties(List<String> excludePaths) {
}
