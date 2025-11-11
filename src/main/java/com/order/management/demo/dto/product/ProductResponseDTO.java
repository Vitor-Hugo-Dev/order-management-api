package com.order.management.demo.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductResponseDTO {

    private UUID id;
    private LocalDateTime createdAt;
    private String name;
    private BigDecimal price;
    private String category;
    private BigDecimal stock;
}