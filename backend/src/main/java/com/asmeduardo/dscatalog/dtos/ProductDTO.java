package com.asmeduardo.dscatalog.dtos;

import com.asmeduardo.dscatalog.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductDTO(
        Long id,

        @Size(min = 5, max = 60, message = "Nome deve ter entre 5 e 60 caracteres")
        @NotBlank(message = "Campo requerido")
        String name,

        @NotBlank(message = "Campo requerido")
        String description,

        @Positive(message = "Preço deve ser um valor positivo")
        Double price,

        String imgUrl,

        @PastOrPresent(message = "A data do produto não pode ser futura")
        Instant date,

        @NotEmpty(message = "Produto deve ter pelo menos uma categoria")
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