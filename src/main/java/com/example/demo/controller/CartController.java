package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Order;
import com.example.demo.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("cartCount", cartService.getTotalQuantity());
        return "cart/list";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id) {
        cartService.addToCart(id);
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Model model) {
        Order order = cartService.checkout();
        model.addAttribute("order", order);
        model.addAttribute("cartCount", cartService.getTotalQuantity());
        return "cart/success";
    }
}
