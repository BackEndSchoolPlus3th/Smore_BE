package com.meossamos.smore.global.initData;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class BaseInitDataDev {
    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
        };
    }

}
