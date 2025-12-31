package com.asmeduardo.dscatalog.dtos;

import com.asmeduardo.dscatalog.entities.Product;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imgUrl,
        Instant date,
        Set<CategoryDTO> categories
) {

    public ProductDTO(Product entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(),
                entity.getDate(),
                entity.getCategories()
                        .stream()
                        .map(CategoryDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}
