package com.order.management.demo.dto.report;

import java.math.BigDecimal;

public record TopSpenderResponseDTO(
        String username,
        BigDecimal totalSpent
) {
}