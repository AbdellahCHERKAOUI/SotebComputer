package com.example.computershop.Controllers;

import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Entities.Product;
import com.example.computershop.Services.ProductService;
import com.example.computershop.Services.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ProductController {

    private ProductService productService;

    private ProductServiceImpl productServiceimp;

    public ProductController(ProductService productService,ProductServiceImpl productServiceImp) {
        this.productService = productService;
        this.productServiceimp=productServiceImp;
    }
   /* @PostMapping("/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                 @RequestParam Product productJson,
                                                 @RequestPart("image") MultipartFile imageFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue((DataInput) productJson,Product.class);
        ProductDTO savedProduct = productService.addProduct(categoryId, product,imageFile);

        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);


    }*/
   @PostMapping(value = "/{categoryId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                @RequestPart("productJson") String productJson,
                                                @RequestParam("image") MultipartFile imageFile) throws IOException {
       ObjectMapper objectMapper = new ObjectMapper();
       Product product = objectMapper.readValue(productJson, Product.class);
       ProductDTO savedProduct = productService.addProduct(categoryId, product, imageFile);

       return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
   }
   /* @GetMapping(value = "/{id}")
    public Product consultProduct(@PathVariable Long id){
        return productServiceimp.getproduct(id).get();
    }*/
}
