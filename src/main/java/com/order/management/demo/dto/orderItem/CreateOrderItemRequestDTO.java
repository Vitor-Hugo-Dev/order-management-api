package com.order.management.demo.dto.orderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class CreateOrderItemRequestDTO {

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private BigDecimal quantity;
}
