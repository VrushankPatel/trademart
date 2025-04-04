package com.umi.trademart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "fix.config")
public class FixProperties {
    private String path;
    private String senderCompId;
    private String targetCompId;
    private int heartbeatInterval;
    private int reconnectInterval;
} 