package com.example.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponseDTO {

    private String productId;
    private String productName;
    private String usp;
    private String brand;
    private String category;
    private String description;

    private String variantId;
    private String img;
    private String colour;
    private String size;
    private String storage;
    private String ram;
    private String capacity;
}