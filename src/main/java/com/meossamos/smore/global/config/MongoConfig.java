package com.meossamos.smore.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoRepositories(basePackages = "com.meossamos.smore.domain.chat.message") // MongoDB Repository 관리
@EnableMongoAuditing // @CreatedDate 활성화
public class MongoConfig {

}

