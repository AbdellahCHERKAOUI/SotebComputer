package com.example.computershop.Services;

import java.io.IOException;
import java.util.List;

import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Entities.Product;
import org.springframework.web.multipart.MultipartFile;


public interface ProductService {
    ImageProduct saveFile(MultipartFile filImage);

    ProductDTO addProduct(Long categoryId, Product product,MultipartFile imageFile ) throws IOException;

    //ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    //ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
     //                                String sortOrder);

    ProductDTO updateProduct(Long productId, Product product);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    //ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
     //                                      String sortOrder);

    String deleteProduct(Long productId);

}
