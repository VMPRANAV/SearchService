package com.example.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingEventDTO {

    private String eventId;
    private ListingEventType eventType;

    private String listingId;
    private String productId;
    private String variantId;
    private String merchantId;
    private String merchantName;

    private double sellingPrice;

    private int availableStock;
    private int soldStock;
}
