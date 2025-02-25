package com.meossamos.smore.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.meossamos.smore.domain")  // JPA Repository 관리
public class JpaConfig {

}
