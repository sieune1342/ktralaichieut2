package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(String keyword, Integer categoryId, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sort));
        return productRepository.searchProducts(keyword, categoryId, pageable);
    }

    private Sort buildSort(String sort) {
        if ("price_asc".equalsIgnoreCase(sort)) {
            return Sort.by("price").ascending();
        }
        if ("price_desc".equalsIgnoreCase(sort)) {
            return Sort.by("price").descending();
        }
        return Sort.by("id").ascending();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
