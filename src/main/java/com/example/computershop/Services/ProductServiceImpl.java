package com.example.computershop.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.computershop.Dto.ProductDTO;

import com.example.computershop.Entities.Category;
import com.example.computershop.Entities.ImageProduct;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.CategoryRepository;
import com.example.computershop.Repositories.ImageProductRepo;
import com.example.computershop.Repositories.ProductRepository;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;
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
    public ProductDTO addProduct(Long categoryId, Product product/*, String imageName*/) throws IOException {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (Product pr : products) {
            if (pr.getProductName().equals(product.getProductName())
                    && pr.getDescription().equals(product.getDescription())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
            //product.setImage("default.png");
            // product.setImagePath(imageProduct.getFilePath());
            product.setCategory(category);

            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepo.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }



    /*public ImageProduct saveFile(MultipartFile file){
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

                    // Create the imageProduct entity and set its properties
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
    }*/public ImageProduct saveFile(MultipartFile file) {
        ImageProduct imageProduct = new ImageProduct();
        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileType = file.getContentType();
            String folderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            try {
                // Set up Cloudinary configuration
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret));

                // Upload file to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", folderName,
                        "public_id", fileName));

                // Create the imageProduct entity and set its properties
                ImageProduct imageProduct1 = new ImageProduct();
                imageProduct1.setFileName(fileName);
                imageProduct1.setFileType(fileType);
                imageProduct1.setFilePath(uploadResult.get("url").toString());

                // Save imageProduct entity to your database
                imageProduct = imageProductRepo.save(imageProduct1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageProduct;
    }


    @Override
    public Product getProductWithImage(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Assuming you have a method to convert Product to ProductDTO including the image URL
        return product;
    }
    @Override
    public ProductDTO updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (!existingProduct.getProductName().equals(updatedProduct.getProductName()) ||
                !existingProduct.getDescription().equals(updatedProduct.getDescription())) {

            // Check if the updated product's name and description combination exists within the same category
            boolean isProductNotPresent = categoryRepo.findById(existingProduct.getCategory().getCategoryId())
                    .map(Category::getProducts)
                    .orElse(Collections.emptyList())
                    .stream()
                    .noneMatch(product -> product.getProductName().equals(updatedProduct.getProductName())
                            && product.getDescription().equals(updatedProduct.getDescription())
                            && !product.getProductId().equals(productId));

            if (isProductNotPresent) {
                existingProduct.setProductName(updatedProduct.getProductName());
                existingProduct.setDescription(updatedProduct.getDescription());


                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setDiscount(updatedProduct.getDiscount());

                double specialPrice = existingProduct.getPrice() - ((existingProduct.getDiscount() * 0.01) * existingProduct.getPrice());
                existingProduct.setSpecialPrice(specialPrice);

                Product savedProduct = productRepo.save(existingProduct);
                return modelMapper.map(savedProduct, ProductDTO.class);
            } else {
                throw new APIException("Product with the new name and description already exists in the category!");
            }
        } else {
            throw new APIException("No changes detected to update the product!");
        }
    }




    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));


        productRepo.deleteById(productId);
        imageProductRepo.deleteById(product.getImageProduct().getId());

        return "Product with productId: " + productId + " deleted successfully !!!";
    }

    @Override
    public ProductDTO getProduct(Long productId) {
        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        ProductDTO productDTO=new ProductDTO();
        productDTO.setProductName(product.getProductName());
        productDTO.setDescription(product.getDescription());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setImagePath(product.getImagePath());
        productDTO.setQuantity(product.getQuantity());

        return productDTO;
    }
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        if(products.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(product.getProductName());
            productDTO.setDescription(product.getDescription());
            productDTO.setDiscount(product.getDiscount());
            productDTO.setImagePath(product.getImagePath());
            productDTO.setQuantity(product.getQuantity());
            productDTOs.add(productDTO);
        }

        return productDTOs;
    }

    public Optional<ImageProduct> getImageProductById(long id) {
        return imageProductRepo.findById(id);
    }


}
