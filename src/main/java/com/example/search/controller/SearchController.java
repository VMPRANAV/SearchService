package com.example.search.controller;

import com.example.search.dto.ProductVariantResponseDTO;
import com.example.search.dto.SearchRequestDTO;
import com.example.search.dto.SearchResponseDTO;
import com.example.search.service.SearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponseDTO> search(@Valid @RequestParam String query,  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        System.out.println("From COntroller");
        return ResponseEntity.ok().body(searchService.search(query,page,size));
    }


}
