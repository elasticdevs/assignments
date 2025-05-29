package com.example.javatest.models;

import jakarta.persistence.*;
import  lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    @Enumerated(EnumType.STRING)
    private Status Status;

    public enum Status {
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
    }

}
