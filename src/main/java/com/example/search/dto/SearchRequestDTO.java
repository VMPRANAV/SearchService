package com.example.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequestDTO {
    @NotBlank(message = "Search Cannot be Empty")
    private String query;
}
