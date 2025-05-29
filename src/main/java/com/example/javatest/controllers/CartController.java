package com.example.javatest.controllers;

import com.example.javatest.dto.AddToCartRequestDTO;
import com.example.javatest.dto.CartResponseDTO;
import com.example.javatest.dto.UpdateCartRequestDTO;
import com.example.javatest.models.Cart;
import com.example.javatest.models.Product;
import com.example.javatest.models.User;
import com.example.javatest.repositories.CartRepository;
import com.example.javatest.repositories.ProductRepository;
import com.example.javatest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private CartResponseDTO mapToResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setUserId(cart.getUser().getId());
        dto.setCartId(cart.getId());
        dto.setStatus(cart.getStatus().name());

        List<CartResponseDTO.CartItemResponse> products = cart.getProducts().stream().map(item -> {
            CartResponseDTO.CartItemResponse p = new CartResponseDTO.CartItemResponse();
            p.setProductId(item.getProduct().getId());
            p.setProductName(item.getProduct().getName());
            p.setQuantity(item.getQuantity());
            return p;
        }).toList();

        dto.setProducts(products);
        return dto;
    }


    @GetMapping("/user/{userId}")
    public CartResponseDTO getActiveCartByUser(@PathVariable Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart is empty."));

        return mapToResponseDTO(cart);
    }

    @PostMapping("/add-product/{userId}")
    public CartResponseDTO addProductToCart(@RequestBody AddToCartRequestDTO request, @PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), Cart.CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus(Cart.CartStatus.ACTIVE);
                    newCart.setProducts(new java.util.ArrayList<>());
                    return newCart;
                });

        boolean exists = false;
        for (Cart.CartItem item : cart.getProducts()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity()); // TO DO: increment by 1 or updated quantity only
                exists = true;
                break;
            }
        }

        if (!exists) {
            cart.getProducts().add(new Cart.CartItem(product, request.getQuantity()));
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToResponseDTO(savedCart);
    }

    @DeleteMapping("/clear-cart/{userId}")
    public CartResponseDTO clearCartByUserId(@PathVariable Long userId) {

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE).orElseThrow( () -> new RuntimeException("Cart is empty."));

        cart.getProducts().clear();

        Cart updatedCart = cartRepository.save(cart);

        return mapToResponseDTO(updatedCart);

    }

    @DeleteMapping("/delete-product/{userId}/{productId}")
    public CartResponseDTO deleteProductFromCart(@PathVariable Long productId, @PathVariable Long userId) {

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE).orElseThrow(() -> new RuntimeException("Cart is empty."));

        boolean removed = cart.getProducts().removeIf(item -> item.getProduct().getId().equals(productId));

        if(!removed) {
            throw new RuntimeException("Product not found");
        }

        Cart updatedCart = cartRepository.save(cart);

        return mapToResponseDTO(updatedCart);

    }

    @PutMapping("/update-product/{userId}")
    public CartResponseDTO updateCart(@RequestBody UpdateCartRequestDTO request, @PathVariable Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), Cart.CartStatus.ACTIVE).orElseThrow(() -> new RuntimeException("Cart is empty."));

        List<Cart.CartItem> existingItems = cart.getProducts();

        List<UpdateCartRequestDTO.ProductQuantity> updatedItems = request.getProducts();

        Map<Long, Integer> updatedMap = updatedItems.stream().collect(Collectors.toMap(UpdateCartRequestDTO.ProductQuantity::getProductId, UpdateCartRequestDTO.ProductQuantity::getQuantity));

        existingItems.removeIf(item -> !updatedMap.containsKey(item.getProduct().getId()));

        Set<Long> existingProductIds = existingItems.stream().map(item -> item.getProduct().getId()).collect(Collectors.toSet());

        for (UpdateCartRequestDTO.ProductQuantity pq : updatedItems) {
            if (existingProductIds.contains(pq.getProductId())) {
                for (Cart.CartItem item : existingItems) {
                    if (item.getProduct().getId().equals(pq.getProductId())) {
                        item.setQuantity(pq.getQuantity());
                        break;
                    }
                }
            } else {
                Product product = productRepository.findById(pq.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + pq.getProductId()));
                cart.getProducts().add(new Cart.CartItem(product, pq.getQuantity()));
            }
        }

        Cart updatedCart = cartRepository.save(cart);
        return mapToResponseDTO(updatedCart);

    }
}

