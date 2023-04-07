package net.provera.productserv.service;

import java.util.List;
import net.provera.productserv.dto.ProductDTO;

public interface ProductService {
	ProductDTO createProduct(ProductDTO productDTO);

    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategoryId(String categoryId);
    ProductDTO getProductById(String id);

    ProductDTO updateProduct(String id, ProductDTO productDTO);

    void deleteProduct(String id);


}
