package net.provera.categoryserv.service;

import net.provera.categoryserv.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
	CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

}
