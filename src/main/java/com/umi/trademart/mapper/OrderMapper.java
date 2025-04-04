package com.umi.trademart.mapper;

import com.umi.trademart.dto.OrderRequest;
import com.umi.trademart.dto.OrderResponse;
import com.umi.trademart.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Order entity and DTOs
 * 
 * @author VrushankPatel
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "fixOrderId", ignore = true)
    @Mapping(target = "ouchOrderId", ignore = true)
    Order toEntity(OrderRequest request);

    OrderResponse toDto(Order order);
}