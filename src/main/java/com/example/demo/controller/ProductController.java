package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam(required = false) Integer categoryId,
                               @RequestParam(defaultValue = "") String sort,
                               @RequestParam(defaultValue = "0") int page) {
        Page<Product> productPage = productService.getProducts(keyword, categoryId, sort, page, 5);
        model.addAttribute("products", productPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cartService.getTotalQuantity());
        return "product/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cartService.getTotalQuantity());
        return "product/add";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }
}
