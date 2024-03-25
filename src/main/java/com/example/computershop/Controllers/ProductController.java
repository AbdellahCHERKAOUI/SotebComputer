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
   public ResponseEntity<ProductDTO> addProductByName(@PathVariable Long categoryId,
                                                      @RequestPart("productJson") String productJson,
                                                      @RequestParam String imageName) throws IOException {
       ObjectMapper objectMapper = new ObjectMapper();
       Product product = objectMapper.readValue(productJson, Product.class);
       ProductDTO savedProduct = productService.addProductByName(categoryId, product, imageName);

       return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
   }
    @GetMapping("/products/{productId}")
    public ProductDTO getProductWithImage(@PathVariable Long productId) {
        return productServiceimp.getProduct(productId);
    }

   /* @GetMapping(value = "/{id}")
    public Product consultProduct(@PathVariable Long id){
        return productServiceimp.getproduct(id).get();
    }*/
   @GetMapping(value = "/allProducts")
   public List<ProductDTO> getAllProducts() {
       return productServiceimp.getAllProducts();
   }
   @PutMapping("/admin/products/{productId}")
   public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product,
                                                   @PathVariable Long productId) {
       ProductDTO updatedProduct = productService.updateProduct(productId, product);

       return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
   }

    @GetMapping("/{id}")

   public ResponseEntity<?> getImageProductById(@PathVariable("id") long id) {
       try {
           return ResponseEntity.ok().body(productServiceimp.getImageProductById(id).orElse(null));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while retrieving image product.");
       }
   }
    @DeleteMapping("/admin/delete/{productId}")
    public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
