package com.example.search.service;

import com.example.search.client.ProductServiceClient;
import com.example.search.dto.ListingEventDTO;
import com.example.search.dto.ProductVariantResponseDTO;
import com.example.search.entity.ProductSearchEntity;
import com.example.search.repository.SearchIndexRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchSyncService {
    @Autowired
    private SearchIndexRepository searchIndexRepository;
    @Autowired
    private ProductServiceClient productServiceClient;

    public void handleListingCreated(ListingEventDTO event) {
        ProductVariantResponseDTO product = productServiceClient.getProductVariant(event.getProductId(), event.getVariantId());

        ProductSearchEntity entity = buildSearchEntity(event, product);

        try {
            searchIndexRepository.indexProduct(entity);
        } catch (IOException e) {
            System.out.println("Invalid Input  " + e.getMessage());
        }
    }

    private ProductSearchEntity buildSearchEntity(ListingEventDTO event, ProductVariantResponseDTO product) {

     ProductSearchEntity searchEntity= ProductSearchEntity.builder()
                .listingId(event.getListingId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getDescription())
                .productUsp(product.getUsp())
                .brand(product.getBrand())
                .category(product.getCategory())
                .variantId(product.getVariantId())
                .img(product.getImg())
                .colour(product.getColour())
                .size(product.getSize())
                .storage(product.getStorage())
                .ram(product.getRam())
                .capacity(product.getCapacity())
                .merchantId(event.getMerchantId())
                .merchantName(event.getMerchantName())
                .price(event.getSellingPrice())
                .currentStock(event.getAvailableStock())
                .productSold(event.getSoldStock())
                .build();

        System.out.println("Combined Search Entity:");
        System.out.println(searchEntity);
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("Combined Search Entity JSON:");
        try {
            System.out.println(
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(searchEntity));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return searchEntity;
    }

    public void handleListingUpdated(ListingEventDTO event) {

        ProductVariantResponseDTO product = productServiceClient.getProductVariant(event.getProductId(), event.getVariantId());

        ProductSearchEntity entity = buildSearchEntity(event, product);
        try {
            searchIndexRepository.indexProduct(entity);
        } catch (IOException e) {
            System.out.println("Invalid Input  "+e.getMessage());
        }
    }

    public void handleListingDeleted(ListingEventDTO event) {
        try {
            searchIndexRepository.deleteProduct(event.getListingId());
        } catch (IOException e) {
            System.out.println("Invalid " + e.getMessage());
        }
    }
}