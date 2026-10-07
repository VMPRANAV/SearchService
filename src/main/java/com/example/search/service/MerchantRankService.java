package com.example.search.service;

import com.example.search.dto.RankingStatsDTO;
import com.example.search.dto.RankProductDTO;
import com.example.search.entity.ProductSearchEntity;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MerchantRankService {
    public List<ProductSearchEntity> rank(List<Hit<ProductSearchEntity>> hits, Map<String, RankingStatsDTO> rankingStats, int page, int size) {
        int requiredResults = (page + 1) * size;
        PriorityQueue<RankProductDTO> topK = new PriorityQueue<>(requiredResults, (a, b) -> Double.compare(a.getScore(), b.getScore()));

        for (Hit<ProductSearchEntity> hit : hits) {

            ProductSearchEntity product = hit.source();
            String variantId = product.getVariantId() != null ? product.getVariantId() : "NO_VARIANT";
            String key = product.getProductId() + "_" + variantId;
            RankingStatsDTO stats = rankingStats.get(key);

            if (stats == null) {
                continue;
            }
            double relevanceScore = hit.score() != null ? hit.score() : 0.0;

            double priceScore = normalizeLower(
                    product.getPrice(),
                    stats.getMinPrice(),
                    stats.getMaxPrice()
            );

            double soldScore = normalizeHigher(
                    product.getProductSold(),
                    stats.getMinSold(),
                    stats.getMaxSold()
            );

            double stockScore = normalizeHigher(
                    product.getCurrentStock(),
                    stats.getMinStock(),
                    stats.getMaxStock()
            );

            double merchantScore = (0.5 * priceScore) + (0.3 * soldScore) + (0.2 * stockScore);

            double finalScore = (0.6 * relevanceScore) + (0.4 * merchantScore);
            System.out.println("Merchant: " + product.getMerchantName() + " Price: " + product.getPrice() + " Sold: " + product.getProductSold() + " Stock: " + product.getCurrentStock() + " Merchant Score: " + merchantScore + " Final Score: " + finalScore);
            RankProductDTO rankProductDTO = RankProductDTO.builder().productSearchEntity(product).score(finalScore).build();
            topK.add(rankProductDTO);
            System.out.println("Added to queue: " + product.getProductId());
            if (topK.size() > requiredResults) {
                topK.poll();
            }

        }
        List<RankProductDTO> ranked = new ArrayList<>(topK);
        ranked.sort(Comparator.comparingDouble(RankProductDTO::getScore).reversed());
        int start = page * size;

        if (start >= ranked.size()) {
            return List.of();
        }


        return ranked.stream().map(RankProductDTO::getProductSearchEntity).toList();
    }

    private double normalizeLower(double value, double min, double max) {
        if (max == min) {
            return 1.0;
        }
        return (max - value) / (max - min);
    }

    private double normalizeHigher(double value, double min, double max) {
        if (max == min) {
            return 1.0;
        }
        return (value - min) / (max - min);
    }
}
