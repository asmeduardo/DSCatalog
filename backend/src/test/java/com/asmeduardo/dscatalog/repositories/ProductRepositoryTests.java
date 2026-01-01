package com.asmeduardo.dscatalog.repositories;

import com.asmeduardo.dscatalog.entities.Product;
import com.asmeduardo.dscatalog.factories.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
        countTotalProducts = 25L;
    }

    @Test
    void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
        Optional<Product> result  = productRepository.findById(existingId);
        Assertions.assertTrue(result .isPresent());
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<Product> result  = productRepository.findById(nonExistingId);
        Assertions.assertFalse(result .isPresent());
    }

    @Test
    void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = ProductFactory.createProduct();

        Product result = productRepository.save(product);

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(countTotalProducts + 1, result.getId());
    }

    @Test
    void deleteShouldDeleteObjectWhenIdExist() {
        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }
}
