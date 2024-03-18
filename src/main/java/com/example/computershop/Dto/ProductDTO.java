package com.example.computershop.Dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    /*private Long productId;*/
    private String productName;
    /*@Lob
    private byte[] image;*/
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    //private double specialPrice;

}