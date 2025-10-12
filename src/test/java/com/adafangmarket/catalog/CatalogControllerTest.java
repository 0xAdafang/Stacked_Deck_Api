package com.adafangmarket.catalog;

import com.adafangmarket.catalog.controller.CatalogController;
import com.adafangmarket.catalog.dto.ProductDto;
import com.adafangmarket.catalog.service.CategoryService;
import com.adafangmarket.catalog.service.ProductService;
import com.adafangmarket.pricing.Price;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        Mockito.when(productService.search(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        mockMvc.perform(get("/api/catalog/products"))
                .andExpect(status().isOk());
    }

    @Test
    void product_with_promo() throws Exception {
        var product = Product.builder()
                .price(Price.builder()
                        .baseAmount(10000L)
                        .promoAmount(8000L)
                        .promoStart(Instant.now().minusSeconds(3600))
                        .promoEnd(Instant.now().plusSeconds(3600))
                        .build())
                .build();

        Mockito.when(productService.getBySlug("test"))
                .thenReturn(new ProductDto(
                        product.getId(),
                        "sku",
                        "name",
                        "slug",
                        null,
                        null,
                        null,
                        8000L,
                        "CAD",
                        8000L,
                        null,
                        true));

        mockMvc.perform(get("/api/catalog/products/test"))
                .andExpect(jsonPath("$.priceAmount").value(8000L));
    }
}