package com.asmeduardo.dscatalog.dtos;

import com.asmeduardo.dscatalog.entities.Category;

public record CategoryDTO(Long id, String name) {

    public CategoryDTO(Category entity) {
        this(entity.getId(), entity.getName());
    }
}
