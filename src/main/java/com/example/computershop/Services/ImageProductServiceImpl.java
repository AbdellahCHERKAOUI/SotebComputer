package com.example.computershop.Services;

import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.ImageProductRepo;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
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
import java.util.UUID;
@Service
public class ImageProductServiceImpl implements ImageProductService{
    private ImageProductRepo imageProductRepo;

    public ImageProductServiceImpl(ImageProductRepo imageProductRepo) {
        this.imageProductRepo = imageProductRepo;
    }

    private static final String UPLOAD_DIR = "C:\\Users\\LENOVO\\Desktop\\images";
    @Override
    public ImageProduct saveImageProduct(MultipartFile imageFile,String imageName){
        List<ImageProduct>imageProducts= imageProductRepo.getAllByFileName(imageName);
        ImageProduct imageProduct = new ImageProduct();
        if (!imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String fileType = imageFile.getContentType();
            String folderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            try {
                // Create the folder that we will store the file in
                String uploadFolderPath = UPLOAD_DIR + "/" + folderName;
                File folder = new File(uploadFolderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                try (InputStream inputStream = imageFile.getInputStream()) {
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
                    Path filePath = Paths.get(uploadFolderPath).resolve(uniqueFileName);
                    for (ImageProduct imageProductList:imageProducts){
                        if (imageProductList.getFileName().equals(imageName)){
                            throw new APIException("image exist");
                        }

                    }
                    // Create the imageProduct entity and set its properties
                    ImageProduct imageProduct1 = new ImageProduct();
                    imageProduct1.setFileName(imageName);
                    imageProduct1.setFileType(fileType);
                    imageProduct1.setFilePath(filePath.toString());
                    imageProduct = imageProduct1;

                    imageProductRepo.save(imageProduct1);


                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageProduct ;
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
