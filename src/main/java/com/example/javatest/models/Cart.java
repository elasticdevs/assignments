package com.example.javatest.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartItem> products;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem {
        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;
        private Integer quantity;
    }

    public enum CartStatus {
        ACTIVE,
        INACTIVE
    }


}