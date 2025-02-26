//package com.meossamos.smore.global.web;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig {
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowCredentials(true) // 쿠키 허용
//                        .allowedHeaders("*")
//                        .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE); // 클라이언트에서 헤더 접근 허용
//
//            }
//        };
//    }
//}