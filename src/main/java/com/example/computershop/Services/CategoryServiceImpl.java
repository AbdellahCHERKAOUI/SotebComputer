package com.example.computershop.Services;

import com.example.computershop.Dto.CategoryDTO;
import com.example.computershop.Entities.Category;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.CategoryRepository;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import com.example.computershop.response.CategoryResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> pageCategories = categoryRepo.findAll(pageDetails);

        List<Category> categories = pageCategories.getContent();

        if (categories.size() == 0) {
            throw new APIException("No category is created till now");
        }

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalElements(pageCategories.getTotalElements());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse;    }

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
