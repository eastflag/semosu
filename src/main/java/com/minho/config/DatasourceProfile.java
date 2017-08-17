package com.minho.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datasource")
@Data
public class DatasourceProfile {
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private Integer initialSize;
    private Integer maxActive;
    private Integer maxIdle;
    private Integer minIdle;
    private Integer maxWait;
    private String validationQuery;
    private Integer validationInterval;
    private Boolean testOnBorrow;
}
