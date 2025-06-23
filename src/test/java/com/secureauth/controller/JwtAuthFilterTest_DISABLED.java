//package com.secureauth.controller;
//
//import com.secureauth.service.TokenBlacklistService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = AuthController.class)
//@AutoConfigureMockMvc(addFilters = true)
//@Import(MockConfig.class)
//class JwtAuthFilterTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TokenBlacklistService tokenBlacklistService;
//
//    @Test
//    void whenTokenIsBlacklisted_thenReturnsJson401() throws Exception {
//        String token = "mocked-token";
//
//        Mockito.when(tokenBlacklistService.isTokenBlacklisted(token)).thenReturn(true);
//
//        mockMvc.perform(post("/v1/auth/signOut")
//                        .with(csrf())
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.errorMessage").value("Token is blacklisted"))
//                .andExpect(jsonPath("$.errorCode").value(401));
//    }
//}
