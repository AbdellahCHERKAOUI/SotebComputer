package com.example.computershop.Services;

import java.io.IOException;
import java.util.List;

import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Entities.Product;
import com.example.computershop.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;


public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product,MultipartFile imageProduct) throws IOException;


    ImageProduct saveFile(MultipartFile filImage);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                     String sortOrder);


    ProductDTO updateProduct(Long productId, Product product);

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
                                           String sortOrder);

    String deleteProduct(Long productId);

    ProductDTO getProduct(Long productId);

    List<ProductDTO> getAllProducts();
}
