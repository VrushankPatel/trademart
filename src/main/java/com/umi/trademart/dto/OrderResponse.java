package com.umi.trademart.dto;

import com.umi.trademart.model.OrderStatus;
import com.umi.trademart.model.OrderType;
import com.umi.trademart.model.Side;
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
    private Long id;
    private String symbol;
    private Side side;
    private Long quantity;
    private Double price;
    private OrderType type;
    private OrderStatus status;
    private String clientOrderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fixOrderId;
    private String ouchOrderId;
}