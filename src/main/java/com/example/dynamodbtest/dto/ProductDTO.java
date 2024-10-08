package com.example.dynamodbtest.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String id;
    private String name;
    private Double price;
    private Long quantity;
}
