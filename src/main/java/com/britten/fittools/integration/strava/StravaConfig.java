package com.britten.fittools.integration.strava;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "strava")
@Getter
@Setter
public class StravaConfig {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
