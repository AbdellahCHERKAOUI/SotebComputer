package com.example.computershop.Controllers;

import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Entities.Product;
import com.example.computershop.Services.ProductService;
import com.example.computershop.Services.ProductServiceImpl;
import com.example.computershop.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping(value = "/api")

public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;

    }
 /*  @PostMapping(value = "/{categoryId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                @RequestPart("productJson") String productJson,
                                                @RequestParam("image") MultipartFile imageFile) throws IOException {
       ObjectMapper objectMapper = new ObjectMapper();
       Product product = objectMapper.readValue(productJson, Product.class);
       ProductDTO savedProduct = productService.addProduct(categoryId, product, imageFile);

       return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
   }*/

   @PostMapping(value = "/{categoryId}")
   public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                @RequestPart Product product,@RequestPart MultipartFile image
                                                ) throws IOException {
       ProductDTO savedProduct = productService.addProduct(categoryId, product,image);
       return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
   }
    @GetMapping("/products/{productId}")
    public ProductDTO getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

   @GetMapping(value = "/allProducts")
   public List<ProductDTO> getAllProducts() {
       return productService.getAllProducts();
   }
   @PutMapping("/admin/products/{productId}")
   public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product,
                                                   @PathVariable Long productId) {
       ProductDTO updatedProduct = productService.updateProduct(productId, product);

       return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
   }

    @DeleteMapping("/admin/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
