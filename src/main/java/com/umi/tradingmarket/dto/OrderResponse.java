package com.umi.tradingmarket.dto;

import com.umi.tradingmarket.model.OrderStatus;
import com.umi.tradingmarket.model.OrderType;
import com.umi.tradingmarket.model.Side;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for order responses
 * 
 * @author VrushankPatel
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;
    private String symbol;
    private BigDecimal price;
    private Long quantity;
    private Side side;
    private OrderType type;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String clientOrderId;
    private String fixOrderId;
    private String ouchOrderId;
}