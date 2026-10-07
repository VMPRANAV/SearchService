package com.example.search.controller;

import com.example.search.client.ProductServiceClient;
import com.example.search.dto.ProductVariantResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private final ProductServiceClient productServiceClient;

    public TestController(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @GetMapping("/product")
    public ProductVariantResponseDTO testProduct() {
        return productServiceClient.getProductVariant("P101", "VO1");
    }


}
