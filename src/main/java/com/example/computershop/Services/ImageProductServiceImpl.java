package com.example.computershop.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.ImageProductRepo;
import com.example.computershop.Repositories.ProductRepository;
import com.example.computershop.cloudinary.CloudinaryUtils;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class ImageProductServiceImpl implements ImageProductService{
    private ImageProductRepo imageProductRepo;
    private ProductRepository productRepo;

    private String apiSecret;
    public ImageProductServiceImpl(ImageProductRepo imageProductRepo,ProductRepository productRepo) {
        this.imageProductRepo = imageProductRepo;
        this.productRepo=productRepo;
    }

    @Override
    public ImageProduct saveImageProduct(MultipartFile imageFile, String imageName,Long productId) {
        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        ImageProduct imageProduct = new ImageProduct();
        List<ImageProduct> imageProducts = imageProductRepo.getAllByFileName(imageName);
        if (!imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String fileType = imageFile.getContentType();

            try {
                // Set up Cloudinary configuration
                Cloudinary cloudinary = CloudinaryUtils.getCloudinary();
                // Upload file to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.asMap(
                        "folder", "your_folder_name", // Optional: specify the folder in Cloudinary
                        "public_id", imageName)); // Use imageName as public_id

                // Create the imageProduct entity and set its properties
                ImageProduct imageProduct1 = new ImageProduct();
                imageProduct1.setProduct(product);
                imageProduct1.setFileName(imageName);
                imageProduct1.setFileType(fileType);
                imageProduct1.setFilePath(uploadResult.get("url").toString()); // Save the URL from Cloudinary

                // Save imageProduct entity to your database
                imageProduct = imageProductRepo.save(imageProduct1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageProduct;
    }
    @Override
    public ImageProduct getImageProduct(String imageName) {
        ImageProduct imageProduct=imageProductRepo.getImageByFileName(imageName);
        return imageProduct;
    }
    @Override
    public String deleteProductImage(Long productId) {
        ImageProduct imageProduct = imageProductRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //List<Cart> carts = cartRepo.findCartsByProductId(productId);

        //carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        imageProductRepo.deleteById(productId);

        return "Product with productId: " + productId + " deleted successfully !!!";
    }

    @Override
    public String deleteByImageName(String fileName) {
        imageProductRepo.deleteImageProductByFileName(fileName);
        return "delete image product succesfuly";
    }
}
