package com.example.computershop.Services;

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
import java.util.*;

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
    public ProductDTO addProductByName(Long categoryId, Product product, String imageName) throws IOException {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

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
            //product.setImage("default.png");
            ImageProduct imageProduct=imageProductRepo.getImageByFileName(imageName);
            product.setImagePath(imageProduct.getFilePath());
            product.setImageProduct(imageProduct);
            product.setCategory(category);

            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepo.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exists !!!");
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

        // Update only if the product name or description has changed
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

                // Perform other updates as necessary
                // For instance, updating price, discount, etc.
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


    /*@Override
    public ProductDTO updateProduct(Long productId, Product product) {

        Product productFromDB = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }

        //product.setImage(productFromDB.getImage());
        product.setProductId(productId);
        product.setCategory(productFromDB.getCategory());

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepo.save(product);

        //List<Cart> carts = cartRepo.findCartsByProductId(productId);
*//*
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));*//*

        return modelMapper.map(savedProduct, ProductDTO.class);
    }
*/




    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //List<Cart> carts = cartRepo.findCartsByProductId(productId);

        //carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

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
            //productDTO.setProductId(product.getProductId()); // Assuming there's a productId field in Product entity
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
