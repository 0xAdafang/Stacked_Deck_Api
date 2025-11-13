package com.stackeddeck.auth;

import com.stackeddeck.auth.controller.AuthController;
import com.stackeddeck.auth.dto.LoginRequest;
import com.stackeddeck.auth.dto.RegisterRequest;
import com.stackeddeck.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired
    AuthService service;

    @Test
    void register_return_202() throws Exception {
        var req = new RegisterRequest("a@b.com","admin","Password123!","Password123!");
        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isAccepted());
        Mockito.verify(service).register(req);
    }

    @Test
    void login_returns_200() throws Exception {
        var req = new LoginRequest("admin","Password123!");
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk());
    }




}
