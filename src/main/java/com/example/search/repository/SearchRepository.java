package com.example.search.repository;

import com.example.search.entity.ProductSearchEntity;
import com.example.search.exception.SearchException;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.MultiMatchQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.TextQueryType;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class SearchRepository {

    @Autowired
    private OpenSearchClient openSearchClient;

    public SearchResponse<ProductSearchEntity> search(String query) throws IOException {

        try {

            Query normalSearch = new MultiMatchQuery.Builder()
                    .query(query)
                    .fields(
                            "productName^5",
                            "brand^4",
                            "category^3",
                            "productUsp^2",
                            "productDescription",
                            "size^4",
                            "colour^4",
                            "capacity^4",
                            "storage^4",
                            "ram^4"
                    )
                    .build()
                    .toQuery();


            Query prefixSearch = new MultiMatchQuery.Builder()
                    .query(query)
                    .type(TextQueryType.PhrasePrefix)
                    .maxExpansions(50)
                    .fields(
                            "productName^5",
                            "productUsp^4",
                            "productDescription^3"
                    )
                    .build()
                    .toQuery();


            Query searchQuery = new BoolQuery.Builder()
                    .should(normalSearch)
                    .should(prefixSearch)
                    .build()
                    .toQuery();


            Aggregation variantAggregation = new Aggregation.Builder().terms(term -> term.field("variantId")
                            .missing(FieldValue.of("NO_VARIANT")))
                    .aggregations("minPrice",
                            a -> a.min(minPrice -> minPrice.field("price")))
                    .aggregations("maxPrice",
                            a -> a.max(maxPrice -> maxPrice.field("price")))
                    .aggregations("minSold",
                            a -> a.min(minSold -> minSold.field("productSold")))
                    .aggregations("maxSold",
                            a -> a.max(maxSold -> maxSold.field("productSold")))
                    .aggregations("minStock",
                            a -> a.min(minStock -> minStock.field("currentStock")))
                    .aggregations("maxStock",
                            a -> a.max(maxStock -> maxStock.field("currentStock")))
                    .build();


            Aggregation productAggregation = new Aggregation.Builder()
                    .terms(term -> term.field("productId"))
                    .aggregations("variants", variantAggregation)
                    .build();


            SearchRequest request = new SearchRequest.Builder().index("products")
                    .query(searchQuery)
                    .size(1000)
                    .aggregations("products", productAggregation)
                    .build();

            System.out.println("SEARCH REQUEST:");
            System.out.println(request);
            return openSearchClient.search(request, ProductSearchEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchException("Failed to search products" + e.getMessage());
        }
    }
}