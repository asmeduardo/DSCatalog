package com.asmeduardo.dscatalog.factories;

import com.asmeduardo.dscatalog.dtos.CategoryDTO;
import com.asmeduardo.dscatalog.entities.Category;

public class CategoryFactory {

    public static Category createCategory() {
        return new Category(null, "Livros");
    }

    public static CategoryDTO createCategoryDTO() {
        Category category = createCategory();
        return new CategoryDTO(category);
    }
}
