package com.stackeddeck.catalog;

import com.stackeddeck.catalog.controller.CatalogController;
import com.stackeddeck.catalog.dto.ProductDto;
import com.stackeddeck.catalog.enums.CardCondition;
import com.stackeddeck.catalog.enums.ProductType;
import com.stackeddeck.catalog.enums.Rarity;
import com.stackeddeck.catalog.service.CategoryService;
import com.stackeddeck.catalog.service.ProductService;
import com.stackeddeck.pricing.Price;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatalogController.class)
public class CatalogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @MockitoBean
    CategoryService categoryService;

    @Test
    void products_ok() throws Exception {
        Page<ProductDto> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 24), 0);

        Mockito.when(productService.search(
                Mockito.anyString(),
                Mockito.any(ProductType.class),
                Mockito.any(UUID.class),
                Mockito.any(Rarity.class),
                Mockito.any(CardCondition.class),
                Mockito.any(Boolean.class),
                Mockito.any(PageRequest.class)
        )).thenReturn(emptyPage);

        mockMvc.perform(get("/api/catalog/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void product_with_promo() throws Exception {
        UUID fakeId = UUID.randomUUID();

        ProductDto dto = new ProductDto(
                fakeId,
                "SKU-CHARIZARD-001",
                "Charizard ex",
                "charizard-ex",
                "Magnifique carte full art...",
                "https://example.com/char1.jpg", // image (first image)
                List.of(
                        "https://example.com/char1.jpg",
                        "https://example.com/char2.jpg"
                ),
                ProductType.SINGLE,
                8000L,
                "CAD",
                10000L,
                null, // categoryId
                null, // categoryName
                Rarity.ULTRA_RARE,
                true
        );

        Mockito.when(productService.getBySlug("charizard-ex"))
                .thenReturn(dto);

        mockMvc.perform(get("/api/catalog/products/charizard-ex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fakeId.toString()))
                .andExpect(jsonPath("$.slug").value("charizard-ex"))
                .andExpect(jsonPath("$.name").value("Charizard ex"))
                .andExpect(jsonPath("$.price").value(8000L))
                .andExpect(jsonPath("$.promoAmount").value(10000L))
                .andExpect(jsonPath("$.currency").value("CAD"))
                .andExpect(jsonPath("$.type").value("SINGLE"))
                .andExpect(jsonPath("$.images").isArray())
                .andExpect(jsonPath("$.images.length()").value(2));
    }
}