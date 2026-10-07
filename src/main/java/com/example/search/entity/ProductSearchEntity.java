package com.example.search.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSearchEntity {
    private String productId;
    private String productName;
    private String productDescription;
    private String productUsp;
    private String category;
    private String brand;

    private String variantId;
    private String img;
    private String colour;
    private String size;
    private String storage;
    private String ram;
    private String capacity;
    private Double price;

    private Integer currentStock;
    private Integer productSold;

    private String listingId;
    private String merchantId;
    private String merchantName;
}
