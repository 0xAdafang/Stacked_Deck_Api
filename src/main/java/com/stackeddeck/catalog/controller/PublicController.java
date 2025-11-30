package com.stackeddeck.catalog.controller;

import com.stackeddeck.catalog.dto.CategoryDto;
import com.stackeddeck.catalog.dto.ProductDto;
import com.stackeddeck.catalog.dto.StatsDto;
import com.stackeddeck.catalog.enums.Rarity;
import com.stackeddeck.catalog.service.CategoryService;
import com.stackeddeck.catalog.service.ProductService;
import com.stackeddeck.catalog.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final StatsService statsService;

    @GetMapping("/featured-categories")
    public List<CategoryDto> getFeaturedCategories() {
        return categoryService.getFeaturedCategories();
    }

    @GetMapping("/featured-products")
    public Page<ProductDto> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return productService.featured(PageRequest.of(page, size));
    }

    @GetMapping("/rarities")
    public List<Rarity> getAvailableRarities(
            @RequestParam(required = false) UUID categoryId
    ) {
        return productService.getAvailableRarities(categoryId);
    }


    @GetMapping("/stats")
    public StatsDto getStats() {
        return statsService.getStats();
    }




}
