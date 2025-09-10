package com.example.whosbookupdate.api;

import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kyobo.api")
@Data
@CommonsLog
public class KyoboApiConfig {

    private String key;
    private String baseUrl;

}
