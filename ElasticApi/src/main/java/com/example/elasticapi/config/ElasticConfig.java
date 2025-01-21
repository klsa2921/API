package com.example.elasticapi.config;

import org.apache.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class ElasticConfig {
    @Value("${spring.elasticsearch.rest.uri}")
    private String elasticsearchHost;

    @Value("${spring.elasticsearch.rest.port}")
    private long elasticsearchPort;

//    @Value("${spring.elasticsearch.rest.connection-timeout}")
//    private String connectionTimeout;
//
//    @Value("${spring.elasticsearch.rest.read-timeout}")
//    private String readTimeout;
    @Bean
    public RestClient restClient() {

        return RestClient.builder(
                new HttpHost(elasticsearchHost, (int) elasticsearchPort, "http")
        ).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
