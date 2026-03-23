package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartService {
    private final List<CartItem> items = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<CartItem> getItems() {
        return items;
    }

    public void addToCart(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return;
        }

        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
            return;
        }

        CartItem newItem = new CartItem();
        newItem.setId(product.getId());
        newItem.setName(product.getName());
        newItem.setImage(product.getImage());
        newItem.setPrice(product.getPrice());
        newItem.setQuantity(1);
        items.add(newItem);
    }

    public void updateQuantity(Long productId, int quantity) {
        items.stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(Math.max(quantity, 1)));
    }

    public void removeFromCart(Long productId) {
        items.removeIf(item -> item.getId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public long getTotal() {
        return items.stream().mapToLong(CartItem::getSubtotal).sum();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public Order checkout() {
        if (items.isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setIsPaid(false);
        order.setTotalAmount(getTotal());

        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : items) {
            Product product = productRepository.findById(item.getId()).orElse(null);
            if (product == null) {
                continue;
            }

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            details.add(detail);
        }

        order.setOrderDetails(details);
        Order savedOrder = orderRepository.save(order);
        clear();
        return savedOrder;
    }
}
