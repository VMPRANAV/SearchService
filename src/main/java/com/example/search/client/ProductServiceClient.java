package com.example.search.client;

import com.example.search.dto.ProductVariantResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProductServiceClient {
    private final RestClient restClient;

    public ProductServiceClient() {
       this.restClient=RestClient.builder()
               .baseUrl("http://10.17.48.129:8082")
               .defaultHeader("Accept","applications/json")
               .build();

    }
    public ProductVariantResponseDTO getProductVariant(String productId, String variantId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/product/getProductAndVaraiantDetails")
                        .queryParam("productId", productId)
                        .queryParam("variantId", variantId)
                        .build())
                .retrieve()
                .body(ProductVariantResponseDTO.class);
    }

}
