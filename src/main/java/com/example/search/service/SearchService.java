package com.example.search.service;

import com.example.search.dto.RankingStatsDTO;
import com.example.search.dto.SearchResponseDTO;
import com.example.search.entity.ProductSearchEntity;
import com.example.search.repository.SearchRepository;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.opensearch.client.opensearch._types.aggregations.Aggregate;
import org.opensearch.client.opensearch._types.aggregations.StringTermsBucket;
import java.util.*;

@Service
public class SearchService {
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private MerchantRankService merchantRankService;

    public SearchResponseDTO search(String query) {
        SearchResponse<ProductSearchEntity> response = searchRepository.search(query);
        List<Hit<ProductSearchEntity>> hits = response.hits().hits();
        Map<String, RankingStatsDTO>rankStats=extractRankingStats(response);


        List<ProductSearchEntity> rankedProducts = merchantRankService.rank(hits,rankStats);

        return SearchResponseDTO.builder()
                .products(rankedProducts)
                .totalResults(rankedProducts.size())
                .build();
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
//    public void indexProduct(Product product, Variant variant, Listing listing, Merchant merchant) {
//
//        ProductSearchEntity document = buildSearchDocument(product, variant, listing, merchant);
//
//        searchRepository.indexProduct(document);
//    }



//    private ProductSearchEntity buildSearchDocument(Product product, Variant variant, Listing listing, Merchant merchant) {
//
//        Map<String, Object> variantAttr =
//                buildVariantAttributes(variant.getSize(), variant.getColor(), variant.getCapacity(), variant.getStorage(), variant.getRam());
//
//        return ProductSearchEntity.builder()
//                .productId(product.getProductId())
//                .productName(product.getProductName())
//                .productDescription(product.getProductDescription())
//                .productUsp(product.getProductUsp())
//                .category(product.getCategory())
//                .brand(product.getBrand())
//
//                .variantId(variant.getVariantId())
//                .variantAttr(variantAttr)
//
//                .price(listing.getPrice())
//                .currentStock(listing.getCurrentStock())
//                .productSold(listing.getProductSold())
//
//                .merchantId(listing.getMerchantId())
//                .merchantName(merchant.getMerchantName())
//
//                .build();
//    }

//    private ProductSearchEntity buildSearchDocument(Product product, Variant variant, Listing listing, Merchant merchant) {
//
//        Map<String, Object> variantAttr =
//                buildVariantAttributes(
//                        variant.getSize(),
//                        variant.getColor(),
//                        variant.getCapacity(),
//                        variant.getStorage(),
//                        variant.getRam()
//                );
//
//        return ProductSearchEntity.builder()
//                .productId(product.getProductId())
//                .productName(product.getProductName())
//                .productDescription(product.getProductDescription())
//                .productUsp(product.getProductUsp())
//                .category(product.getCategory())
//                .brand(product.getBrand())
//
//                .variantId(variant.getVariantId())
//                .variantAttr(variantAttr)
//
//                .price(listing.getPrice())
//                .currentStock(listing.getCurrentStock())
//                .productSold(listing.getProductSold())
//
//                .merchantId(listing.getMerchantId())
//                .merchantName(merchant.getMerchantName())
//
//                .build();
//    }
//    private Map<String, Object> buildVariantAttributes(String size, String color, String capacity, String storage, String ram) {
//
//        Map<String, Object> variantAttr = new HashMap<>();
//
//        if (size != null) {
//            variantAttr.put("size", size);
//        }
//
//        if (color != null) {
//            variantAttr.put("color", color);
//        }
//
//        if (capacity != null) {
//            variantAttr.put("capacity", capacity);
//        }
//
//        if (storage != null) {
//            variantAttr.put("storage", storage);
//        }
//
//        if (ram != null) {
//            variantAttr.put("ram", ram);
//        }
//
//        return variantAttr;
//    }
}