package com.example.search.entity;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductSearchEntity {
    private String productId;
    private String productName;
    private String productDescription;
    private String productUsp;
    private String category;
    private String brand;

    private String variantId;
    private Map<String, Object> variantAttr;
    private Double price;
    private Integer currentStock;
    private Integer productSold;

    private String merchantId;
    private String merchantName;
}
