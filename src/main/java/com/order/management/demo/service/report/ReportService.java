package com.order.management.demo.service.report;

import com.order.management.demo.dto.report.MonthlyRevenueResponse;
import com.order.management.demo.dto.report.TopSpenderResponseDTO;
import com.order.management.demo.dto.report.UserAverageTicketResponseDTO;
import com.order.management.demo.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final OrderRepository orderRepository;

    public List<TopSpenderResponseDTO> getTopSpenders() {
        return orderRepository.findTopSpenders(PageRequest.of(0, 5));
    }

    public List<UserAverageTicketResponseDTO> getAverageTicketPerUser() {
        return orderRepository.findUserAverageTicket();
    }


    public MonthlyRevenueResponse getTotalRevenueForMonth(YearMonth month) {
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal revenue = orderRepository.findTotalRevenueForPeriod(start, end);

        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        return new MonthlyRevenueResponse(month, revenue);
    }
}