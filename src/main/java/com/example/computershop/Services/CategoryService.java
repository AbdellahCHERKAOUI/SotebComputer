package com.example.computershop.Services;


import com.example.computershop.Dto.CategoryDTO;
import com.example.computershop.Entities.Category;
import com.example.computershop.response.CategoryResponse;

public interface CategoryService {

    CategoryDTO createCategory(Category category);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);

}