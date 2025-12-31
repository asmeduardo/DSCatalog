package com.asmeduardo.dscatalog.factories;

import com.asmeduardo.dscatalog.dtos.ProductDTO;
import com.asmeduardo.dscatalog.entities.Category;
import com.asmeduardo.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createProduct() {
        Product product = new Product(null, "Telefone", "Aparelho celular", 1000.0,
                "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(1L, "Eletrônicos"));
        return  product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
