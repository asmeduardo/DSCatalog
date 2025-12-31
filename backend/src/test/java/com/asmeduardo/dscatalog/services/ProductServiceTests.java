package com.asmeduardo.dscatalog.services;

import com.asmeduardo.dscatalog.dtos.ProductDTO;
import com.asmeduardo.dscatalog.entities.Category;
import com.asmeduardo.dscatalog.entities.Product;
import com.asmeduardo.dscatalog.factories.CategoryFactory;
import com.asmeduardo.dscatalog.factories.ProductFactory;
import com.asmeduardo.dscatalog.repositories.CategoryRepository;
import com.asmeduardo.dscatalog.repositories.ProductRepository;
import com.asmeduardo.dscatalog.services.exceptions.DatabaseException;
import com.asmeduardo.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setup(){
        existingId = 1L;
        nonExistingId = 999L;
        dependentId = 3L;
        product = ProductFactory.createProduct();
        category = CategoryFactory.createCategory();
        productDTO = ProductFactory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(productRepository).deleteById(dependentId);

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = productService.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).save(ArgumentMatchers.any());
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistingId, productDTO);
        });
    }

    @Test
    void findByIdShouldReturnAProductDTOWhenIdExists() {
        ProductDTO result = productService.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(productDTO, result);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingId));
    }

    @Test
    void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findAll(pageable);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }
}
