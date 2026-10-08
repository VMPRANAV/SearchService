package com.example.search.client;

import com.example.search.dto.ProductVariantResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://192.168.1.102:8000")
public interface ProductServiceClient {

    @GetMapping("/product/getProductAndVariantDetails")
    ProductVariantResponseDTO getProductVariant (
            @RequestParam String productId,
            @RequestParam String variantId
    );
}