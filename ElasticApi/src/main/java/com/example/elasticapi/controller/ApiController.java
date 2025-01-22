package com.example.elasticapi.controller;

import com.example.elasticapi.service.ElasticsearchService;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/api")
public class ApiController {

    @RequestMapping(value="/test",method= RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("test successfull");
    }

    private final ElasticsearchService elasticsearchService;

    @Autowired
    public ApiController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @RequestMapping(value="/searchByUsingClient",method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchByUsingClient(@RequestBody String query) {
        try {
            String response= elasticsearchService.searchWithElasticSearchClient(query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @RequestMapping(value="/searchByUsingRestAPI",method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchByUsingRestAPI(@RequestBody String query) {
        try {
            String response= elasticsearchService.searchWithRestAPI(query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @RequestMapping(value="/ingestByUsingRestAPI",method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ingestByUsingRestAPI(@RequestBody String query) {
        try {
//            IndexResponse response= elasticsearchService.indexUsingElasticSearchClient(query);

            return ResponseEntity.ok("ingest sucessful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @RequestMapping(value="/searchWithFilterByUsingRestAPI",method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchWithFilterByUsingRestAPI(@RequestBody String query) {
        try {
            Map<String, Object> filters=new HashMap<>();
            filters.put("relation","father");
            String response= elasticsearchService.searchWithElasticSearchClientWithFilter(query,filters);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
