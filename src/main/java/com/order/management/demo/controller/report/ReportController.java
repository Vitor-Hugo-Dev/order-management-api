package com.order.management.demo.controller.report;

import com.order.management.demo.dto.report.MonthlyRevenueResponse;
import com.order.management.demo.dto.report.TopSpenderResponseDTO;
import com.order.management.demo.dto.report.UserAverageTicketResponseDTO;
import com.order.management.demo.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;


    @GetMapping("/top-spenders")
    public ResponseEntity<List<TopSpenderResponseDTO>> getTopSpenders() {
        return ResponseEntity.ok(reportService.getTopSpenders());
    }


    @GetMapping("/average-ticket")
    public ResponseEntity<List<UserAverageTicketResponseDTO>> getAverageTicket() {
        return ResponseEntity.ok(reportService.getAverageTicketPerUser());
    }


    @GetMapping("/monthly-revenue")
    public ResponseEntity<MonthlyRevenueResponse> getMonthlyRevenue(@RequestParam(name = "month", required = false) YearMonth month) {
        YearMonth queryMonth = (month == null) ? YearMonth.now() : month;
        return ResponseEntity.ok(reportService.getTotalRevenueForMonth(queryMonth));
    }
}