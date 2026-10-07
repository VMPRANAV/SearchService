package com.example.search.service;

import com.example.search.dto.RankingStatsDTO;
import com.example.search.dto.SearchResponseDTO;
import com.example.search.entity.ProductSearchEntity;
import com.example.search.exception.SearchException;
import com.example.search.exception.SearchIndexException;
import com.example.search.repository.SearchRepository;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.opensearch.client.opensearch._types.aggregations.Aggregate;
import org.opensearch.client.opensearch._types.aggregations.StringTermsBucket;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private MerchantRankService merchantRankService;

    public SearchResponseDTO search(String query, int page, int size) {
        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page cannot be negative");
            }

            SearchResponse<ProductSearchEntity> response = searchRepository.search(query);
            List<Hit<ProductSearchEntity>> hits = response.hits().hits();
            Map<String, RankingStatsDTO> rankStats = extractRankingStats(response);
            List<ProductSearchEntity> rankedProducts = merchantRankService.rank(hits, rankStats,page,size);
            int totalResults= hits.size();
            int start= page*size;
            int end= Math.min(start+size,totalResults);
            List<ProductSearchEntity>paginatedProducts=rankedProducts.subList(start,end);
            return SearchResponseDTO.builder().products(paginatedProducts)
                    .page(page)
                    .size(size)
                    .totalPages((int)Math.ceil((double)totalResults/size))
                    .totalResults(totalResults)
                    .build();
        } catch (IOException e) {
            throw new SearchIndexException("Unable to connect OpenSearch " + e.getMessage());
        } catch (SearchException e) {
            throw new SearchException("Unable to perform search " + e.getMessage());
        }

    }

    private Map<String, RankingStatsDTO> extractRankingStats(SearchResponse<ProductSearchEntity> response) {
        Map<String, RankingStatsDTO> rankingStats = new HashMap<>();
        Aggregate productsAggregation = response.aggregations().get("products");
        if (productsAggregation == null) {
            return rankingStats;
        }
        for (StringTermsBucket productBucket : productsAggregation.sterms().buckets().array()) {
            String productId = productBucket.key().toString();
            Aggregate variantsAggregation = productBucket.aggregations().get("variants");
            if (variantsAggregation == null) {
                continue;
            }
            for (StringTermsBucket variantBucket : variantsAggregation.sterms().buckets().array()) {
                String variantId = variantBucket.key().toString();
                Aggregate minPrice = variantBucket.aggregations().get("minPrice");
                Aggregate maxPrice = variantBucket.aggregations().get("maxPrice");
                Aggregate minSold = variantBucket.aggregations().get("minSold");
                Aggregate maxSold = variantBucket.aggregations().get("maxSold");

                Aggregate minStock = variantBucket.aggregations().get("minStock");

                Aggregate maxStock = variantBucket.aggregations().get("maxStock");

                RankingStatsDTO stats = RankingStatsDTO.builder()
                        .minPrice(minPrice.min().value())
                        .maxPrice(maxPrice.max().value())
                        .minSold(minSold.min().value())
                        .maxSold(maxSold.max().value())
                        .minStock(minStock.min().value())
                        .maxStock(maxStock.max().value())
                        .build();
                String key = productId + "_" + variantId;

                rankingStats.put(key, stats);
            }
        }

        return rankingStats;
    }
}