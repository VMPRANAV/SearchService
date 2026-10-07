package com.example.search.client;

import com.example.search.dto.ProductVariantResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductServiceClient {

    @GetMapping("/products/variant")
    ProductVariantResponseDTO getProductVariant(
            @RequestParam String productId,
            @RequestParam String variantId
    );
}