package net.provera.categoryserv.service.impl;

import net.provera.categoryserv.dao.CategoryRepository;
import net.provera.categoryserv.dao.model.Category;
import net.provera.categoryserv.dto.CategoryDTO;
import net.provera.categoryserv.exception.ResourceNotFoundException;
import net.provera.categoryserv.mapper.CategoryMapper;
import net.provera.categoryserv.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired
    private final CategoryRepository categoryRepository;
	@Autowired
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Category category = categoryMapper.toCategory(categoryDTO);
		Category savedCategory =categoryRepository.save(category);
		
		return categoryMapper.toCategoryDTO(savedCategory);
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		List<Category> categories= categoryRepository.findAll();
		return categories.stream().map(categoryMapper::toCategoryDTO).collect(Collectors.toList());
	}

	@Override
	public CategoryDTO getCategoryById(Long id) {
		
		Category category = categoryRepository.findById(id)
				 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return categoryMapper.toCategoryDTO(category);
		
	}

	@Override
	public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
		var existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
       existingCategory.setName(categoryDTO.getName());
      
   
      var updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toCategoryDTO(updatedCategory);
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        categoryRepository.delete(category);
		
	}

}
