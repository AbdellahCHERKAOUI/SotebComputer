package com.example.computershop.Controllers;

import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Services.ImageProductServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api")
public class ImageProductController {

    private ImageProductServiceImpl imageProductServiceImpl;
    public ImageProductController(ImageProductServiceImpl imageProductServiceImpl) {
        this.imageProductServiceImpl = imageProductServiceImpl;
    }
    @PostMapping(value = "/saveImage/{productId}")
    public ImageProduct saveImageProduct(@RequestParam MultipartFile imageFile,@RequestParam String imageName,@PathVariable Long productId){
        ImageProduct imageProduct= imageProductServiceImpl.saveImageProduct(imageFile,imageName,productId);
        return imageProduct;

    }
}
