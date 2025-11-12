package com.order.management.demo.dto.order;

import com.order.management.demo.dto.orderItem.CreateOrderItemRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDTO {

    @NotEmpty
    @Valid
    private List<CreateOrderItemRequestDTO> items;
}