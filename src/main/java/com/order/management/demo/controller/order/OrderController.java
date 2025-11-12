package com.order.management.demo.controller.order;

import com.order.management.demo.dto.order.CreateOrderRequestDTO;
import com.order.management.demo.dto.order.OrderResponseDTO;
import com.order.management.demo.dto.order.UpdateOrderRequestDTO;
import com.order.management.demo.model.User;
import com.order.management.demo.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal User currentUser,
                                                        @Valid @RequestBody CreateOrderRequestDTO request) {
        OrderResponseDTO response = orderService.createOrder(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/pay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDTO> payForOrder(@PathVariable UUID orderId,
                                                        @AuthenticationPrincipal User currentUser) {
        OrderResponseDTO response = orderService.processOrderPayment(orderId, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@AuthenticationPrincipal User currentUser) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCurrentUser(currentUser);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable UUID orderId, @AuthenticationPrincipal User currentUser,
                                                        @Valid @RequestBody UpdateOrderRequestDTO request) {
        OrderResponseDTO response = orderService.updateOrder(orderId, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable UUID orderId, @AuthenticationPrincipal User currentUser) {
        OrderResponseDTO response = orderService.cancelOrder(orderId, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
