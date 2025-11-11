package com.order.management.demo.service.product;


import com.order.management.demo.config.exceptionHandler.ResourceNotFoundException;
import com.order.management.demo.dto.product.ProductRequestDTO;
import com.order.management.demo.dto.product.ProductResponseDTO;
import com.order.management.demo.model.Product;
import com.order.management.demo.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ProductResponseDTO toResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts(Boolean inStock) {

        List<Product> products;

        if (Boolean.TRUE.equals(inStock)) {
            products = productRepository.findByStockGreaterThan(BigDecimal.ZERO);
        } else {
            products = productRepository.findAll();
        }

        return products.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(UUID id) {
        return productRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .stock(request.getStock())
                .build();

        Product savedProduct = productRepository.save(product);
        return toResponseDTO(savedProduct);
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());

        Product updatedProduct = productRepository.save(product);
        return toResponseDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto", "id", id);
        }

        productRepository.deleteById(id);
    }
}