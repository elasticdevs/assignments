package com.example.javatest.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {
    private Long userId;
    private Long cartId;
    private List<CartItemResponse> products;
    private String status;

    @Data
    public static class CartItemResponse {
        private Long productId;
        private String productName;
        private Integer quantity;
    }
}
