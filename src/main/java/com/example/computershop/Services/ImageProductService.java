package com.example.computershop.Services;

import com.example.computershop.Entities.ImageProduct;
import org.springframework.web.multipart.MultipartFile;

public interface ImageProductService {
    ImageProduct saveImageProduct(MultipartFile imageFile,String imageName);

    ImageProduct getImageProduct(String imageName);

    String deleteProductImage(Long productId);
    String deleteByImageName(String fileName);
}
