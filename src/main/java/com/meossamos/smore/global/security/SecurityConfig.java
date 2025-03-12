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
                // 로그인한 사용자만 채팅(WS, SSE) 관련 엔드포인트에 접근하도록 익명 인증 비활성화
                .anonymous(anonymous -> anonymous.disable())
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/v1/recruitmentArticles/detail").permitAll()
                        .requestMatchers("/api/v1/recruitmentArticle/clip").permitAll()
                    //    .requestMatchers("/api/v1/recruitmentArticles/**").permitAll()
                        .requestMatchers("/api/v1/study/**").permitAll()
                        .requestMatchers("/api/v1/member/login").permitAll()
                        .requestMatchers("/api/v1/member/signup").permitAll()
                        // WebSocket 관련 경로는 모두 허용하고, 인증은 StompChannelInterceptor에서 처리
                        .requestMatchers("/ws/**").permitAll()  // WebSocket 엔드포인트 추가 허용
                        .requestMatchers("/topic/**").permitAll() // 구독 경로 허용
                        .requestMatchers("/app/**").permitAll() // 메시지 송신 경로 허용
                        .requestMatchers("/ws/info/**").permitAll()
                        .requestMatchers("/api/member/refresh").permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/sse/connect").permitAll()
                        .requestMatchers("/api/current-user").permitAll()
                        .requestMatchers("/api/study/my-studies").permitAll()


                        .requestMatchers("/add").permitAll()
                        .requestMatchers("/api/study/my-studies").permitAll()
                        .requestMatchers("/sse/connect/**").permitAll()


                        .requestMatchers("/error").permitAll()

                        .requestMatchers("/test/**").permitAll()
                        .anyRequest().authenticated()
                )
                .with(jwtSecurityConfigBean.getJwtSecurityConfig(),(config) -> config.configure(http))
                .addFilterAfter(new StudyAccessCheckFilter(studyMemberService), JwtFilter.class);
        ;

        return http.build();
    }
}