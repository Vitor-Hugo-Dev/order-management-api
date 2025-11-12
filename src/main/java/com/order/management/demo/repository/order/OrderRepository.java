package com.order.management.demo.repository.order;

import com.order.management.demo.dto.report.TopSpenderResponseDTO;
import com.order.management.demo.dto.report.UserAverageTicketResponseDTO;
import com.order.management.demo.model.Order;
import com.order.management.demo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserOrderByCreatedAtDesc(User currentUser);

    @Query("SELECT new com.order.management.demo.dto.report.TopSpenderResponseDTO(o.user.name, SUM(o.totalAmount)) " +
            "FROM Order o WHERE o.status = 'PAID' " +
            "GROUP BY o.user.name " +
            "ORDER BY SUM(o.totalAmount) DESC")
    List<TopSpenderResponseDTO> findTopSpenders(Pageable pageable);

    @Query("SELECT new com.order.management.demo.dto.report.UserAverageTicketResponseDTO(o.user.name, AVG(o.totalAmount)) " +
            "FROM Order o WHERE o.status = 'PAID' " +
            "GROUP BY o.user.name")
    List<UserAverageTicketResponseDTO> findUserAverageTicket();

    @Query("SELECT SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.status = 'PAID' AND o.createdAt BETWEEN :start AND :end")
    BigDecimal findTotalRevenueForPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
