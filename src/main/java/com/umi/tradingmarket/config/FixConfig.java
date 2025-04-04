package com.umi.tradingmarket.config;

import com.umi.tradingmarket.fix.FixApplication;
import com.umi.tradingmarket.fix.FixMessageHandler;
import com.umi.tradingmarket.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import quickfix.*;

import java.io.InputStream;

/**
 * Configuration for FIX protocol
 * @author VrushankPatel
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "fix.config")
@ConfigurationPropertiesBinding
@Getter
@Setter
public class FixConfig {
    private final FixMessageHandler fixMessageHandler;
    private final OrderService orderService;

    @Value("${fix.config.path:config/FIX44.xml}")
    private String path;
    
    @Value("${fix.config.sender-comp-id:SIMULATOR}")
    private String senderCompId;
    
    @Value("${fix.config.target-comp-id:CLIENT}")
    private String targetCompId;
    
    @Value("${fix.config.heartbeat-interval:30}")
    private int heartbeatInterval;
    
    @Value("${fix.config.reconnect-interval:5}")
    private int reconnectInterval;

    public FixConfig(FixMessageHandler fixMessageHandler, OrderService orderService) {
        this.fixMessageHandler = fixMessageHandler;
        this.orderService = orderService;
        log.info("FixConfig initialized with properties: path={}, senderCompId={}, targetCompId={}, heartbeatInterval={}, reconnectInterval={}", 
            path, senderCompId, targetCompId, heartbeatInterval, reconnectInterval);
    }

    @PostConstruct
    public void logProperties() {
        log.info("FixConfig properties after initialization: path={}, senderCompId={}, targetCompId={}, heartbeatInterval={}, reconnectInterval={}", 
            path, senderCompId, targetCompId, heartbeatInterval, reconnectInterval);
    }

    /**
     * Create FIX application
     * @return The FIX application
     */
    @Bean
    public FixApplication fixApplication() {
        log.info("Creating FIX application with path: {}", path);
        return new FixApplication(orderService, fixMessageHandler);
    }

    /**
     * Create FIX session settings
     * @return The FIX session settings
     */
    @Bean
    public SessionSettings sessionSettings() {
        try {
            log.info("Loading FIX configuration from path: {}", path);
            if (path == null) {
                log.error("FIX configuration path is null. Current properties: path={}, senderCompId={}, targetCompId={}, heartbeatInterval={}, reconnectInterval={}", 
                    path, senderCompId, targetCompId, heartbeatInterval, reconnectInterval);
                throw new IllegalStateException("FIX configuration path is null. Please check application.yml");
            }
            
            // Load the FIX configuration file from the classpath
            ClassPathResource resource = new ClassPathResource(path);
            if (!resource.exists()) {
                log.error("FIX configuration file not found at path: {}", path);
                throw new IllegalStateException("FIX configuration file not found at path: " + path);
            }
            
            InputStream inputStream = resource.getInputStream();
            SessionSettings settings = new SessionSettings(inputStream);
            
            // Override settings from application.yml if needed
            settings.setString("SenderCompID", senderCompId);
            settings.setString("TargetCompID", targetCompId);
            settings.setString("HeartBtInt", String.valueOf(heartbeatInterval));
            settings.setString("ReconnectInterval", String.valueOf(reconnectInterval));
            
            log.info("FIX configuration loaded successfully");
            return settings;
        } catch (Exception e) {
            log.error("Failed to load FIX configuration file: {}", path, e);
            throw new RuntimeException("Failed to load FIX configuration file: " + path, e);
        }
    }
}