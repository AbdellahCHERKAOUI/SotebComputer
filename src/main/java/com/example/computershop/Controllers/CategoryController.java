package com.example.computershop.Controllers;

import com.example.computershop.Dto.CategoryDTO;
import com.example.computershop.Entities.Category;
import com.example.computershop.Services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody Category category) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(category);

        return new ResponseEntity<CategoryDTO>(savedCategoryDTO, HttpStatus.CREATED);
    }
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody Category category,
                                                      @PathVariable Long categoryId) {
        CategoryDTO categoryDTO = categoryService.updateCategory(category, categoryId);

        return new ResponseEntity<CategoryDTO>(categoryDTO, HttpStatus.OK);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
