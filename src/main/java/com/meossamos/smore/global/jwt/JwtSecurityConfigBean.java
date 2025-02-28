package com.meossamos.smore.global.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JwtSecurityConfigBean {
    JwtSecurityConfig jwtSecurityConfig;

    public JwtSecurityConfigBean(TokenProvider tokenProvider) {
        this.jwtSecurityConfig = new JwtSecurityConfig(tokenProvider);

    }

    @Bean
    public JwtSecurityConfig getJwtSecurityConfig(){
        return jwtSecurityConfig;
    }
}

