package net.provera.productserv.mapper;



import net.provera.productserv.dao.model.Product;
import net.provera.productserv.dto.ProductDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	 ProductDTO toProductDTO(Product product);
	 Product toProduct(ProductDTO productDTO);
	List<ProductDTO> toProductDTOs(List<Product> products);

	List<Product> toProducts(List<ProductDTO> productDTOs);
}
