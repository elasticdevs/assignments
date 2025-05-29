package com.example.javatest.controllers;

import com.example.javatest.dto.OrderStatusRequestDTO;
import com.example.javatest.models.Cart;
import com.example.javatest.models.Order;
import com.example.javatest.models.User;
import com.example.javatest.repositories.CartRepository;
import com.example.javatest.repositories.OrderRepository;
import com.example.javatest.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public OrderController(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @RequestMapping("/{orderId}")
    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

    }

    @PostMapping("/place-order/{userId}")
    public Order placeOrder(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE).orElseThrow(() -> new RuntimeException("Cart not found"));

        double totalPrice = 0.0;

        for(Cart.CartItem item : cart.getProducts() ){
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.Status.IN_PROGRESS);

        cart.setStatus(Cart.CartStatus.INACTIVE);
        cartRepository.save(cart);

        return orderRepository.save(order);
    }

    @PutMapping("/update-order-status")
    public Order updateOrderStatus(OrderStatusRequestDTO request){
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }

}
