package com.meossamos.smore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = {"com.meossamos.smore.domain"})
public class SmoreBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmoreBeApplication.class, args);
    }

}
