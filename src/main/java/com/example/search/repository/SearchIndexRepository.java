package com.example.search.repository;

import com.example.search.config.OpenSearchConfig;
import com.example.search.entity.ProductSearchEntity;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class SearchIndexRepository {
    @Autowired
    private OpenSearchClient openSearchClient;

    public void indexProduct(ProductSearchEntity product) throws IOException {
//check if the product listing id exists if yes over rides
        openSearchClient.index(indexRequest -> indexRequest.index("products").id(product.getListingId()).document(product));
    }

    public void deleteProduct(String listingId) throws IOException {

        openSearchClient.delete(deleteRequest -> deleteRequest.index("products").id(listingId));
    }
}
