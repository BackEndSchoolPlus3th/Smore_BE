package com.meossamos.smore.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

@Configuration
public class ElasticClientConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {

        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.add("ngrok-skip-browser-warning", "true");

        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }}, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SSL context", e);
        }

        return ClientConfiguration.builder()
                .connectedTo("72e5-221-149-72-194.ngrok-free.app:443") // 명시적으로 포트 443 지정
                .usingSsl(sslContext, (hostname, session) -> true)
                .withDefaultHeaders(defaultHeaders)
                .withConnectTimeout(Duration.ofSeconds(10))
                .withSocketTimeout(Duration.ofSeconds(60))
                .build();
    }
}
