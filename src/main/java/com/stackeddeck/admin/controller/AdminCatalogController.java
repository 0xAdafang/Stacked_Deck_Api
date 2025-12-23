package com.stackeddeck.admin.controller;


import com.stackeddeck.catalog.dto.CategoryDto;
import com.stackeddeck.catalog.dto.ProductCreateRequest;
import com.stackeddeck.catalog.dto.ProductDto;
import com.stackeddeck.catalog.service.CategoryService;
import com.stackeddeck.catalog.service.ProductService;
import com.stackeddeck.checkout.Order;
import com.stackeddeck.checkout.enums.OrderStatus;
import com.stackeddeck.checkout.repo.OrderRepository;
import com.stackeddeck.checkout.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/catalog")
@RequiredArgsConstructor
public class AdminCatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderRepository orderRepository;



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ProductDto create(@Valid @RequestBody ProductCreateRequest req) {
        return productService.create(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{id}")
    public ProductDto update(@PathVariable UUID id, @Valid @RequestBody ProductCreateRequest req) {
        return productService.update(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public CategoryDto create(@Valid @RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories/{id}")
    public CategoryDto update(@PathVariable UUID id, @Valid @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
