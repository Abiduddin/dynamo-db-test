package com.example.dynamodbtest.repository;

import com.example.dynamodbtest.dto.ProductDTO;
import com.example.dynamodbtest.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductRepository {
    private final DynamoDbTable<Product> productTable;

    public ProductRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.productTable = dynamoDbEnhancedClient.table("test_product", TableSchema.fromBean(Product.class));
    }

    public List<ProductDTO> getAllProducts() {
        PageIterable<Product> products = productTable.scan();
        List<ProductDTO> allProducts = new ArrayList<>();
        products.items().forEach(x -> allProducts.add(convertToDTO(x)));
        return allProducts;
    }

    public ProductDTO getProductById(String id) {
        Product product = productTable.getItem(r -> r.key(k -> k.partitionValue(id)));
        return convertToDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productTable.putItem(product);
        return convertToDTO(product);
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Product product = productTable.getItem(r -> r.key(k -> k.partitionValue(id)));
        if (product != null) {
            BeanUtils.copyProperties(productDTO, product);
            product = productTable.updateItem(product);
        }
        return convertToDTO(product);
    }

    public void deleteProduct(String id) {
        Product product = productTable.getItem(r -> r.key(k -> k.partitionValue(id)));
        if (product != null)
            productTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        if (product == null)
            return null;
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }


}
