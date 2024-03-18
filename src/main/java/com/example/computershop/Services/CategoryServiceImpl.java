package com.example.computershop.Services;

import com.example.computershop.Dto.CategoryDTO;
import com.example.computershop.Entities.Category;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepo;

    private ModelMapper modelMapper;

    private ProductService productService;

    public CategoryServiceImpl(CategoryRepository categoryRepo,ProductService productService) {
        this.categoryRepo = categoryRepo;
        this.productService=productService;
    }

     @Override
     public CategoryDTO createCategory(Category category) {
         Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());

         if (savedCategory != null) {
             throw new RuntimeException("Category with the name '" + category.getCategoryName() + "' already exists !!!");
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

    @Override
    public String deleteCategory(Long categoryId) {
        return null;
    }

  /*  @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category"*//*, "categoryId", categoryId*//*));

        List<Product> products = category.getProducts();

        products.forEach(product -> {
            productService.deleteProduct(product.getProductId());
        });

        categoryRepo.delete(category);

        return "Category with categoryId: " + categoryId + " deleted successfully !!!";
    }*/
}
