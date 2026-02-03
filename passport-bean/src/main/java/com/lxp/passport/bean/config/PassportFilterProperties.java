package com.lxp.passport.bean.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "passport.filter")
public class PassportFilterProperties {

    /**
     * Ant 패턴 경로 목록. 기본값은 빈 리스트.
     */
    private List<String> excludePaths = new ArrayList<>();

}
