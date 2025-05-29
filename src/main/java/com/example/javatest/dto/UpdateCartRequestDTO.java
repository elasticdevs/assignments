package com.example.javatest.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCartRequestDTO {

    private Long userId;
    private List<ProductQuantity> products;

    @Data
    public static class ProductQuantity {
        private Long productId;
        private Integer quantity;
    }

}


