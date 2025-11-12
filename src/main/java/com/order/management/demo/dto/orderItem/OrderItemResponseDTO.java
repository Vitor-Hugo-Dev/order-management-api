package com.order.management.demo.dto.orderItem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponseDTO {

    private UUID productId;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subtotal;
}
