//package com.secureauth.controller;
//
//import com.secureauth.security.JwtTokenProvider;
//import com.secureauth.service.TokenBlacklistService;
//import com.secureauth.service.UserService;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@TestConfiguration
//public class MockConfig {
//
//    @Bean
//    public TokenBlacklistService tokenBlacklistService() {
//        return Mockito.mock(TokenBlacklistService.class);
//    }
//
//    @Bean
//    public JwtTokenProvider jwtTokenProvider() {
//        return Mockito.mock(JwtTokenProvider.class);
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return Mockito.mock(UserDetailsService.class);
//    }
//
//    @Bean
//    public UserService userService() {
//        return Mockito.mock(UserService.class);
//    }
//
//}
