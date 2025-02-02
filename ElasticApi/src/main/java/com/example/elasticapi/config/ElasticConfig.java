package com.example.elasticapi.config;

import org.apache.http.HttpHost;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
@Configuration
public class ElasticConfig {
    @Value("${spring.elasticsearch.rest.uri}")
    private String elasticsearchHost;

    @Value("${spring.elasticsearch.rest.port}")
    private int elasticsearchPort;

    @Value("${spring.elasticsearch.index.name}")
    public String getElasticsearchHost;

//    @Value("${spring.elasticsearch.rest.connection-timeout}")
//    private String connectionTimeout;
//
//    @Value("${spring.elasticsearch.rest.read-timeout}")
//    private String readTimeout;

    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient() {

        return RestClient.builder(
                new HttpHost(elasticsearchHost, (int) elasticsearchPort, "http")
        ).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http") // Adjust the host and port as needed
                        new HttpHost(elasticsearchHost, elasticsearchPort, "http") // Adjust the host and port as needed
                )
        );
    }
}
