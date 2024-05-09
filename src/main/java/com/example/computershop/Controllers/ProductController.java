package com.example.computershop.Controllers;

import com.example.computershop.Config.AppConstants;
import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Entities.Product;
import com.example.computershop.Services.ProductService;
import com.example.computershop.Services.ProductServiceImpl;
import com.example.computershop.exceptions.ResourceNotFoundException;
import com.example.computershop.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/pro")
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
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                @RequestPart Product product,@RequestPart MultipartFile image
                                                ) throws IOException {
       ProductDTO savedProduct = productService.addProduct(categoryId, product,image);
       return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
   }
   @GetMapping("/products")
   @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
   public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }
    @GetMapping("/categories/{categoryId}/products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,
                sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }
    @GetMapping("/products/keyword/{keyword}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }
    @GetMapping("/products/{productId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ProductDTO getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

 /*  @GetMapping(value = "/allProducts")
   public List<ProductDTO> getAllProducts() {
       return productService.getAllProducts();
   }*/
   @PutMapping("/admin/products/{productId}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product,
                                                   @PathVariable Long productId) {
       ProductDTO updatedProduct = productService.updateProduct(productId, product);

       return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
   }

    @DeleteMapping("/admin/delete/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
