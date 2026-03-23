package com.example.demo.model;

import lombok.Data;

@Data
public class CartItem {
    private Long id;
    private String name;
    private String image;
    private long price;
    private int quantity;

    public long getSubtotal() {
        return price * quantity;
    }
}
