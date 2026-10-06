package com.example.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingStatsDTO {

    private double minPrice;
    private double maxPrice;

    private double minSold;
    private double maxSold;

    private double minStock;
    private double maxStock;
}