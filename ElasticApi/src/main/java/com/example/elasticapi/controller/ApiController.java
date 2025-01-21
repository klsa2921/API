package com.example.elasticapi.controller;

import com.example.elasticapi.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.elasticsearch.action.search.SearchResponse;


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

    @RequestMapping(value="/search",method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> search(@RequestBody String query) {
        try {
            String response= elasticsearchService.search3(query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
