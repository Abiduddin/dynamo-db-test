package com.example.dynamodbtest.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.dynamodbtest.dto.ProductDTO;
import com.example.dynamodbtest.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepository {
    private final DynamoDBMapper dynamoDBMapper;

    public List<ProductDTO> getAllProducts() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Product> products = dynamoDBMapper.scan(Product.class, scanExpression);
        return products.stream().map(this::convertToDTO).toList();
    }

    public ProductDTO getProductById(String id) {
        Product product = dynamoDBMapper.load(Product.class, id);
        return convertToDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        dynamoDBMapper.save(product);
        return convertToDTO(product);
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Product product = dynamoDBMapper.load(Product.class, id);
        BeanUtils.copyProperties(productDTO, product);
        dynamoDBMapper.save(product);
        return convertToDTO(product);
    }

    public void deleteProduct(String id) {
        Product product = dynamoDBMapper.load(Product.class, id);
        if (product != null)
            dynamoDBMapper.delete(product);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        if (product == null)
            return null;
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }


}
