package com.example.computershop.Services;

import com.example.computershop.Dto.CategoryDTO;
import com.example.computershop.Entities.Category;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.CategoryRepository;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepo;

    private ModelMapper modelMapper;

    private ProductService productService;
    private ImageProductService imageProductService;

    public CategoryServiceImpl(CategoryRepository categoryRepo,ProductService productService,ImageProductService imageProductService,ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.productService=productService;
        this.imageProductService=imageProductService;
        this.modelMapper=modelMapper;
    }

     @Override
     public CategoryDTO createCategory(Category category) {
         Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());

         if (savedCategory != null) {
             throw new APIException("Category with the name '" + category.getCategoryName() + "' already exists !!!");
         }

         savedCategory = categoryRepo.save(category);

         return modelMapper.map(category, CategoryDTO.class);
     }
    @Override
    public CategoryDTO updateCategory(Category category, Long categoryId) {

        Category savedCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"/*, "categoryId", categoryId*/));

        category.setCategoryId(categoryId);

        savedCategory = categoryRepo.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

  /*  @Override
    public String deleteCategory(Long categoryId) {
        return null;
    }
*/
  @Transactional
    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> products = category.getProducts();

      for (Product product : products) {
          productService.deleteProduct(product.getProductId());
          imageProductService.deleteByImageName(product.getImageProduct().getFileName());
      }

        categoryRepo.delete(category);

        return "Category with categoryId: " + categoryId + " deleted successfully !!!";
    }
}
