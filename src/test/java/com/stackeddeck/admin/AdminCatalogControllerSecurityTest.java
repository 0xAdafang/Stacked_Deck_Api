package com.stackeddeck.admin;

import com.stackeddeck.admin.controller.AdminCatalogController;
import com.stackeddeck.catalog.service.CategoryService;
import com.stackeddeck.catalog.service.ProductService;
import com.stackeddeck.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCatalogController.class)
@Import(SecurityConfig.class)
public class AdminCatalogControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean ProductService productService;
    @MockitoBean CategoryService categoryService;

    @Test
    void create_requires_admin() throws Exception {
        var req = """
      {"sku":"SEAL-ETB-151","name":"ETB 151","slug":"etb-151","type":"ETB","baseAmount":10999,"currency":"CAD"}
      """;
        mockMvc.perform(post("/api/admin/catalog/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isForbidden()); // pas de JWT admin => 403
    }
}