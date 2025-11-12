package com.order.management.demo.service.order;

import com.order.management.demo.config.exceptionHandler.InsufficientStockException;
import com.order.management.demo.config.exceptionHandler.ResourceNotFoundException;
import com.order.management.demo.dto.order.CreateOrderRequestDTO;
import com.order.management.demo.dto.order.OrderResponseDTO;
import com.order.management.demo.dto.order.UpdateOrderRequestDTO;
import com.order.management.demo.dto.orderItem.CreateOrderItemRequestDTO;
import com.order.management.demo.dto.orderItem.OrderItemResponseDTO;
import com.order.management.demo.model.Order;
import com.order.management.demo.model.OrderItem;
import com.order.management.demo.model.Product;
import com.order.management.demo.model.User;
import com.order.management.demo.model.enuns.OrderStatus;
import com.order.management.demo.model.enuns.Role;
import com.order.management.demo.repository.order.OrderRepository;
import com.order.management.demo.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request, User currentUser) {
        Order newOrder = Order.builder()
                .user(currentUser)
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (CreateOrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productService.getProductEntityById(itemRequest.getProductId());

            BigDecimal price = product.getPrice();

            BigDecimal subtotal = price.multiply(itemRequest.getQuantity()).setScale(2, RoundingMode.HALF_UP);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(price)
                    .build();

            newOrder.addItem(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        newOrder.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(newOrder);

        return toOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCurrentUser(User currentUser) {
        return orderRepository.findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO processOrderPayment(UUID orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", orderId));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Você não tem permissão para pagar este pedido.");
        }
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Este pedido não está mais pendente de pagamento.");
        }

        try {
            for (OrderItem item : order.getItems()) {
                productService.debitStockAndLock(item.getProduct().getId(), item.getQuantity());
            }

        } catch (InsufficientStockException e) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            throw e;
        }

        order.setStatus(OrderStatus.PAID);
        Order paidOrder = orderRepository.save(order);

        return toOrderResponse(paidOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrder(UUID orderId, UpdateOrderRequestDTO request, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", orderId));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Você não tem permissão para alterar este pedido.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Apenas pedidos pendentes podem ser alterados.");
        }

        order.getItems().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productService.getProductEntityById(itemRequest.getProductId());

            BigDecimal price = product.getPrice();
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(price)
                    .build();

            order.addItem(orderItem);
            totalAmount = totalAmount.add(price.multiply(itemRequest.getQuantity()));
        }

        order.setTotalAmount(totalAmount);
        Order updatedOrder = orderRepository.save(order);

        return toOrderResponse(updatedOrder);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", orderId));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = order.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Você não tem permissão para cancelar este pedido.");
        }

        if (order.getStatus() == OrderStatus.PAID) {
            for (OrderItem item : order.getItems()) {
                productService.restockItemAndLock(item.getProduct().getId(), item.getQuantity());
            }
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Este pedido já está cancelado.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        return toOrderResponse(cancelledOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return orderRepository.findAll(sort).stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO toOrderResponse(Order order) {
        List<OrderItemResponseDTO> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .priceAtPurchase(item.getPriceAtPurchase())
                        .subtotal(item.getPriceAtPurchase().multiply(item.getQuantity()))
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }

}