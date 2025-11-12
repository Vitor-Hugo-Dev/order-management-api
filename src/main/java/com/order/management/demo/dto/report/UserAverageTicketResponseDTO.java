package com.order.management.demo.dto.report;

public record UserAverageTicketResponseDTO(
        String username,
        Double averageTicket
) {
}