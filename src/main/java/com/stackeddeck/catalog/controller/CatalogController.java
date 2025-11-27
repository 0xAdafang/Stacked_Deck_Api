package com.stackeddeck.catalog.controller;

import com.stackeddeck.catalog.dto.CategoryDto;
import com.stackeddeck.catalog.dto.ProductDto;
import com.stackeddeck.catalog.enums.CardCondition;
import com.stackeddeck.catalog.enums.ProductType;
import com.stackeddeck.catalog.enums.Rarity;
import com.stackeddeck.catalog.service.ProductService;
import com.stackeddeck.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CatalogController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/products")
    public Page<ProductDto> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) ProductType type,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) Rarity rarity,
            @RequestParam(required = false) CardCondition condition,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        var parts = sort.split(",");
        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(parts[1].trim()), parts[0].trim())
        );
        return productService.search(q, type, categoryId, rarity, condition, inStock, pageable);
    }

    @GetMapping("/products/{slug}")
    public ProductDto bySlug(@PathVariable String slug) {
        return productService.getBySlug(slug);
    }

    @GetMapping("/categories")
    public List<CategoryDto> categories() {
        return categoryService.treeRoot();
    }

}