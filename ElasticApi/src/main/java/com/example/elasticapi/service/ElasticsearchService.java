package com.example.elasticapi.service;

import org.springframework.stereotype.Service;
import org.apache.http.entity.ContentType;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.client.RestTemplate;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ElasticsearchService {
    @Autowired
    private RestClient restClient;



    @Autowired
    private RestTemplate restTemplate;

    public String search( String queryJson) throws IOException {
        // Send the request to Elasticsearch with the query provided in the body
        Request request = new Request("POST", "/employee-parent-child25/_search"); // Replace '/your-index/_search' with your Elasticsearch index and endpoint
        request.setEntity(new org.apache.http.entity.StringEntity(queryJson, ContentType.APPLICATION_JSON));

        // Perform the request and get the response
        Response response = restClient.performRequest(request);


        // Convert the response to a string and return it
        return new String(response.getEntity().getContent().readAllBytes());
    }

    public String search2(String query){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://192.168.1.27:9200/employee-parent-child20/_search", request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return "Error calling API: " + response.getStatusCode();
        }
    }

    private final RestHighLevelClient client;

    public ElasticsearchService(RestHighLevelClient client) {
        this.client = client;
    }

    public String search3(String query) throws IOException {
        try {
            boolean isConnected = client.ping(RequestOptions.DEFAULT);
            System.out.println("Elasticsearch connected: " + isConnected);
            SearchRequest searchRequest = new SearchRequest("employee-parent-child25");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//            searchSourceBuilder.query(QueryBuilders.matchQuery("fieldName", query));

            searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            return searchResponse.toString();
        }catch (Exception e){
            System.err.println("Elasticsearch error: " + e.getStackTrace());
            System.err.println("Details: " + e.getMessage());
            return e.getMessage().toString();
        }

    }
}
