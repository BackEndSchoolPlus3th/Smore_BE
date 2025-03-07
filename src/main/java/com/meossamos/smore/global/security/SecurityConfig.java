package com.meossamos.smore.global.security;

import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import com.meossamos.smore.global.filter.StudyAccessCheckFilter;
import com.meossamos.smore.global.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtSecurityConfigBean jwtSecurityConfigBean;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, StudyMemberService studyMemberService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/v1/recruitmentArticles/detail").permitAll()
                        .requestMatchers("/api/v1/recruitmentArticle/clip").permitAll()
                    //    .requestMatchers("/api/v1/recruitmentArticles/**").permitAll()
                        .requestMatchers("/api/v1/study/**").permitAll()
                        .requestMatchers("/api/member/login").permitAll()
                        .requestMatchers("/api/member/signup").permitAll()
                        .requestMatchers("/api/chatrooms/**").permitAll() // 채팅 테스트용
                        .requestMatchers("/api/member/refresh").permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/sse/connect").permitAll()
                        .requestMatchers("/api/current-user").permitAll()
                        .requestMatchers("/api/study/my-studies").permitAll()


                        .requestMatchers("/add").permitAll()
                        .requestMatchers("/api/study/my-studies").permitAll()
                        .requestMatchers("/sse/connect/**").permitAll()


                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .with(jwtSecurityConfigBean.getJwtSecurityConfig(),(config) -> config.configure(http))
                .addFilterAfter(new StudyAccessCheckFilter(studyMemberService), JwtFilter.class);
        ;

        return http.build();
    }
}