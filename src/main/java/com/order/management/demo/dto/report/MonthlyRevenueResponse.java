package com.order.management.demo.dto.report;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyRevenueResponse(
        YearMonth month,
        BigDecimal totalRevenue
) {
}