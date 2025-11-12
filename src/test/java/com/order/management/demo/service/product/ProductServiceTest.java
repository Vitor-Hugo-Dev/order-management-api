package com.order.management.demo.service.product;

import com.order.management.demo.config.exceptionHandler.InsufficientStockException;
import com.order.management.demo.model.Product;
import com.order.management.demo.repository.product.ProductRepository;
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
@DisplayName("Testes do service de Produto (ProductService)")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Deve debitar o estoque corretamente quando há quantidade suficiente")
    void debitStockAndLock_QuandoEstoqueSuficiente_DeveDebitarEstoque() {
        UUID productId = UUID.randomUUID();
        Product mockProduct = Product.builder()
                .name("Notebook")
                .stock(new BigDecimal("10.00"))
                .build();
        mockProduct.setId(productId);

        BigDecimal quantityToDebit = new BigDecimal("3.00");

        when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(mockProduct));

        productService.debitStockAndLock(productId, quantityToDebit);

        verify(productRepository).save(argThat(savedProduct ->
                savedProduct.getStock().compareTo(new BigDecimal("7.00")) == 0
        ));
        assertThat(mockProduct.getStock()).isEqualByComparingTo(new BigDecimal("7.00"));
    }

    @Test
    @DisplayName("Deve lançaar 'InsufficientStockException' quando o estoque for insuficiente")
    void debitStockAndLock_QuandoEstoqueInsuficiente_DeveLancarExcecao() {
        UUID productId = UUID.randomUUID();
        Product mockProduct = Product.builder()
                .name("Notebook")
                .stock(new BigDecimal("2.00"))
                .build();
        mockProduct.setId(productId);

        BigDecimal quantityToDebit = new BigDecimal("3.00");

        when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(mockProduct));

        assertThatThrownBy(() -> productService.debitStockAndLock(productId, quantityToDebit))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Estoque insuficiente para Notebook");

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve repor (adicionar) o estoque corretamente ao cancelar um pedido")
    void restockItemAndLock_QuandoChamado_DeveAdicionarEstoque() {
        UUID productId = UUID.randomUUID();
        Product mockProduct = Product.builder()
                .name("Notebook")
                .stock(new BigDecimal("10.00"))
                .build();

        mockProduct.setId(productId);

        BigDecimal quantityToAdd = new BigDecimal("5.00");

        when(productRepository.findByIdForUpdate(productId)).thenReturn(Optional.of(mockProduct));

        productService.restockItemAndLock(productId, quantityToAdd);

        verify(productRepository).save(argThat(savedProduct ->
                savedProduct.getStock().compareTo(new BigDecimal("15.00")) == 0
        ));
    }
}