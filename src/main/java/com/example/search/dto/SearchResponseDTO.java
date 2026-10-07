package com.example.search.dto;

import com.example.search.entity.ProductSearchEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponseDTO {
    private List<ProductSearchEntity> products;
    private int page;
    private int size;
    private int totalPages;
    private long totalResults;

}
