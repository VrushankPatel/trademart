package com.umi.trademart.config;

import com.umi.trademart.fix.FixApplication;
import com.umi.trademart.fix.FixMessageHandler;
import com.umi.trademart.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import quickfix.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration for FIX protocol
 * @author VrushankPatel
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "fix.config")
public class FixConfig {
    private String path;
    private String senderCompId;
    private String targetCompId;
    private int heartbeatInterval;
    private int reconnectInterval;

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
    public FixApplication fixApplication(OrderService orderService, FixMessageHandler fixMessageHandler) {
        log.info("Creating FIX application");
        return new FixApplication(orderService, fixMessageHandler);
    }

    /**
     * Create FIX session settings
     * @return The FIX session settings
     */
    @Bean
    public SessionSettings sessionSettings() throws ConfigError {
        log.info("Loading FIX configuration from {}", path);
        try {
            // Create a temporary file with the FIX settings
            Path tempFile = Files.createTempFile("fix", ".cfg");
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(tempFile.toFile()))) {
                writer.println("[DEFAULT]");
                writer.println("ConnectionType=initiator");
                writer.println("FileStorePath=data/fix");
                writer.println("FileLogPath=data/fix/log");
                writer.println("StartTime=00:00:00");
                writer.println("EndTime=00:00:00");
                writer.println("UseDataDictionary=Y");
                writer.println("DataDictionary=" + path);
                writer.println("ValidateUserDefinedFields=N");
                writer.println("ValidateIncomingMessage=N");
                writer.println("RefreshOnLogon=Y");
                writer.println("ResetOnLogon=Y");
                writer.println("ResetOnLogout=Y");
                writer.println("ResetOnDisconnect=Y");
                writer.println("SendRedundantResendRequests=Y");
                writer.println("PersistMessages=Y");
                writer.println();
                writer.println("[SESSION]");
                writer.println("BeginString=FIX.4.4");
                writer.println("SenderCompID=" + senderCompId);
                writer.println("TargetCompID=" + targetCompId);
                writer.println("SocketConnectHost=localhost");
                writer.println("SocketConnectPort=9876");
                writer.println("HeartBtInt=" + heartbeatInterval);
                writer.println("ReconnectInterval=" + reconnectInterval);
            }

            // Create session settings from the temporary file
            SessionSettings settings = new SessionSettings(tempFile.toString());
            log.info("FIX configuration loaded successfully");
            return settings;
        } catch (IOException e) {
            log.error("Failed to create FIX configuration: {}", e.getMessage());
            throw new ConfigError("Failed to create FIX configuration: " + e.getMessage());
        }
    }

    @Bean
    public ThreadedSocketInitiator threadedSocketInitiator(SessionSettings settings, FixApplication fixApplication) throws ConfigError {
        log.info("Creating FIX initiator");
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        ThreadedSocketInitiator initiator = new ThreadedSocketInitiator(fixApplication, messageStoreFactory, settings, logFactory, messageFactory);
        log.info("FIX initiator created successfully");
        return initiator;
    }
}