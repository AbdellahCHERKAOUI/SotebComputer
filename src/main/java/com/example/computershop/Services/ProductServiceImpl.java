package com.example.computershop.Services;

import com.example.computershop.Dto.ProductDTO;

import com.example.computershop.Entities.Category;
import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.CategoryRepository;
import com.example.computershop.Repositories.ImageProductRepo;
import com.example.computershop.Repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

    private CategoryRepository categoryRepo;

    private ProductRepository productRepo;
    private ImageProductRepo imageProductRepo;

    private ModelMapper modelMapper;

    public ProductServiceImpl(CategoryRepository categoryRepo, ProductRepository productRepo,ModelMapper modelMapper,ImageProductRepo imageProductRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.modelMapper=modelMapper;
        this.imageProductRepo=imageProductRepo;

    }

    private static final String UPLOAD_DIR = "C:\\Users\\LENOVO\\Desktop\\images";
    @Override
    public ProductDTO addProduct(Long categoryId, Product product, MultipartFile imageFile) throws IOException {

            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category"/*, "categoryId", categoryId*/));

            boolean isProductNotPresent = true;

            List<Product> products = category.getProducts();

            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductName().equals(product.getProductName())
                        && products.get(i).getDescription().equals(product.getDescription())) {

                    isProductNotPresent = false;
                    break;
                }
            }

            if (isProductNotPresent) {
                /*product.setImage("default.png");*/
               product.setImageProduct(saveFile(imageFile));

                product.setCategory(category);

                double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
                product.setSpecialPrice(specialPrice);

                Product savedProduct = productRepo.save(product);

                return modelMapper.map(savedProduct, ProductDTO.class);
            } else {
                throw new RuntimeException("Product already exists !!!");
            }
    }
    public ImageProduct saveFile(MultipartFile file){
        ImageProduct imageProduct = new ImageProduct();
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();
            String folderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            try {
                // Create the folder that we will store the file in
                String uploadFolderPath = UPLOAD_DIR + "/" + folderName;
                File folder = new File(uploadFolderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                try (InputStream inputStream = file.getInputStream()) {
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
                    Path filePath = Paths.get(uploadFolderPath).resolve(uniqueFileName);

                    // Create the Attachment entity and set its properties
                    ImageProduct imageProduct1 = new ImageProduct();
                    imageProduct1.setFileName(fileName);
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
    public ProductDTO updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        return null;
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product"/*, "productId", productId*/));

        //List<Cart> carts = cartRepo.findCartsByProductId(productId);

        //carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepo.deleteById(productId);

        return "Product with productId: " + productId + " deleted successfully !!!";
    }
    public Optional<Product> getproduct(Long idProduct){
        return productRepo.findById(idProduct);
    }
}
