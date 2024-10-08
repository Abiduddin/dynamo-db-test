package com.example.dynamodbtest.controller;

import com.example.dynamodbtest.dto.ProductDTO;
import com.example.dynamodbtest.model.Product;
import com.example.dynamodbtest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable String id) {
        return productRepository.getProductById(id);
    }

    @PostMapping()
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        log.info("Creating product {}", productDTO);
        ProductDTO product = productRepository.getProductById(productDTO.getId());
        if (product == null) {
            return productRepository.createProduct(productDTO);
        } else {
            log.info("Product Exist: {}", product);
            return product;
        }
    }

    @PutMapping()
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) {
        return productRepository.updateProduct(productDTO.getId(), productDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productRepository.deleteProduct(id);
    }

}
