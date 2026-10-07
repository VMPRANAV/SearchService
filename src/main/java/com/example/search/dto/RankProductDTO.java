package com.example.search.dto;

import com.example.search.entity.ProductSearchEntity;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RankProductDTO {
    private ProductSearchEntity productSearchEntity;
    private double score;

}
