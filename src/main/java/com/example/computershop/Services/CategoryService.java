package com.example.computershop.Services;


import com.example.computershop.Dto.CategoryDTO;
import com.example.computershop.Entities.Category;

public interface CategoryService {

    CategoryDTO createCategory(Category category);

   // CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);

}