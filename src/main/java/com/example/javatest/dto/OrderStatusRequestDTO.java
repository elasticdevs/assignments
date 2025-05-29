package com.example.javatest.dto;

import com.example.javatest.models.Order;
import lombok.Data;

@Data
public class OrderStatusRequestDTO {
    private Long orderId;
    private Order.Status status;
}
