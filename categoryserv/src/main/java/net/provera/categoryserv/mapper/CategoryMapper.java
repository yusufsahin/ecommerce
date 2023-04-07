package net.provera.categoryserv.mapper;

import net.provera.categoryserv.dao.model.Category;
import net.provera.categoryserv.dto.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	CategoryDTO toCategoryDTO(Category category);
	Category toCategory(CategoryDTO categoryDTO);

}
