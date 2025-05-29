package com.example.javatest.dto;

import lombok.Data;

@Data
public class AddToCartRequestDTO {
    private Long productId;
    private Integer quantity;
}
