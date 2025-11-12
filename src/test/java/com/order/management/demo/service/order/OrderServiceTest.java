package com.order.management.demo.service.order;

import com.order.management.demo.config.exceptionHandler.InsufficientStockException;
import com.order.management.demo.dto.order.OrderResponseDTO;
import com.order.management.demo.model.Order;
import com.order.management.demo.model.OrderItem;
import com.order.management.demo.model.Product;
import com.order.management.demo.model.User;
import com.order.management.demo.model.enuns.OrderStatus;
import com.order.management.demo.model.enuns.Role;
import com.order.management.demo.repository.order.OrderRepository;
import com.order.management.demo.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do service de Pedidos (OrderService)")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private User mockUser;
    private User mockAdmin;
    private Order mockPendingOrder;
    private Order mockPaidOrder;
    private Product mockProduct;
    private OrderItem mockOrderItem;

    @BeforeEach
    void setUp() {
        mockUser = User.builder().role(Role.USER).build();
        mockUser.setId(UUID.randomUUID());

        mockAdmin = User.builder().role(Role.ADMIN).build();
        mockAdmin.setId(UUID.randomUUID());

        mockProduct = Product.builder().name("Test Product").build();
        mockProduct.setId(UUID.randomUUID());

        mockOrderItem = OrderItem.builder()
                .product(mockProduct)
                .quantity(new BigDecimal("2.0"))
                .priceAtPurchase(new BigDecimal("10.00"))
                .build();

        mockOrderItem.setId(UUID.randomUUID());

        mockPendingOrder = Order.builder()
                .user(mockUser)
                .status(OrderStatus.PENDING)
                .build();

        mockPendingOrder.setId(UUID.randomUUID());
        mockPendingOrder.addItem(mockOrderItem);

        mockPaidOrder = Order.builder()
                .user(mockUser)
                .status(OrderStatus.PAID)
                .build();
        mockPaidOrder.setId(UUID.randomUUID());
        OrderItem itemPago = OrderItem.builder()
                .product(mockProduct)
                .quantity(new BigDecimal("2.0"))
                .priceAtPurchase(new BigDecimal("10.00"))
                .build();
        itemPago.setId(UUID.randomUUID());
        mockPaidOrder.addItem(itemPago);
    }

    @Test
    @DisplayName("Deve processar o pagamento (Status PAID) quando o estoque for suficiente")
    void processOrderPayment_QuandoEstoqueSuficiente_DeveMudarStatusParaPaid() {
        when(orderRepository.findById(mockPendingOrder.getId())).thenReturn(Optional.of(mockPendingOrder));
        doNothing().when(productService).debitStockAndLock(any(UUID.class), any(BigDecimal.class));
        when(orderRepository.save(any(Order.class))).thenReturn(mockPendingOrder);

        OrderResponseDTO response = orderService.processOrderPayment(mockPendingOrder.getId(), mockUser);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(productService, times(1)).debitStockAndLock(mockProduct.getId(), new BigDecimal("2.0"));
        verify(orderRepository, times(1)).save(argThat(order -> order.getStatus() == OrderStatus.PAID));
    }

    @Test
    @DisplayName("Deve cancelar o pedido (Status CANCELLED) e lançar exceção se o estoque for insuficiente")
    void processOrderPayment_QuandoEstoqueInsuficiente_DeveMudarStatusParaCancelledELancarExcecao() {
        when(orderRepository.findById(mockPendingOrder.getId())).thenReturn(Optional.of(mockPendingOrder));
        doThrow(new InsufficientStockException("Estoque insuficiente"))
                .when(productService).debitStockAndLock(mockProduct.getId(), new BigDecimal("2.0"));

        assertThatThrownBy(() -> orderService.processOrderPayment(mockPendingOrder.getId(), mockUser))
                .isInstanceOf(InsufficientStockException.class);

        verify(orderRepository, times(1)).save(argThat(
                order -> order.getStatus() == OrderStatus.CANCELLED
        ));
        assertThat(mockPendingOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("Deve lançaar 'IllegalArgumentException' ao tentar pagar um pedido que não está PENDENTE")
    void processOrderPayment_QuandoPedidoNaoEstaPendente_DeveLancarExcecao() {
        when(orderRepository.findById(mockPaidOrder.getId())).thenReturn(Optional.of(mockPaidOrder));

        assertThatThrownBy(() -> orderService.processOrderPayment(mockPaidOrder.getId(), mockUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Este pedido não está mais pendente de pagamento.");

        verify(productService, never()).debitStockAndLock(any(), any());
    }

    @Test
    @DisplayName("Deve cancelar um pedido PAGO e repor o estoque")
    void cancelOrder_QuandoStatusForPaid_DeveReporEstoqueECancelar() {
        when(orderRepository.findById(mockPaidOrder.getId())).thenReturn(Optional.of(mockPaidOrder));
        doNothing().when(productService).restockItemAndLock(any(UUID.class), any(BigDecimal.class));
        when(orderRepository.save(any(Order.class))).thenReturn(mockPaidOrder);

        OrderResponseDTO response = orderService.cancelOrder(mockPaidOrder.getId(), mockUser);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(productService, times(1)).restockItemAndLock(mockProduct.getId(), new BigDecimal("2.0"));
        verify(orderRepository).save(argThat(order -> order.getStatus() == OrderStatus.CANCELLED));
    }

    @Test
    @DisplayName("Deve cancelar um pedido PENDENTE sem repor o estoque")
    void cancelOrder_QuandoStatusForPending_DeveCancelarSemReporEstoque() {
        when(orderRepository.findById(mockPendingOrder.getId())).thenReturn(Optional.of(mockPendingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockPendingOrder);

        OrderResponseDTO response = orderService.cancelOrder(mockPendingOrder.getId(), mockUser);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(productService, never()).restockItemAndLock(any(), any());
    }

    @Test
    @DisplayName("Deve permitir que um ADMIN cancele o pedido de outro usuário")
    void cancelOrder_QuandoUsuarioForAdminMasNaoDono_DeveCancelar() {
        when(orderRepository.findById(mockPaidOrder.getId())).thenReturn(Optional.of(mockPaidOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockPaidOrder);

        OrderResponseDTO response = orderService.cancelOrder(mockPaidOrder.getId(), mockAdmin);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(productService, times(1)).restockItemAndLock(any(), any());
    }
}