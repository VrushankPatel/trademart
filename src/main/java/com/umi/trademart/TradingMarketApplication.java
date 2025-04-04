package com.umi.trademart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the TradeMart Trading Gateway Simulator
 * @author VrushankPatel
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class TradingMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradingMarketApplication.class, args);
    }
}