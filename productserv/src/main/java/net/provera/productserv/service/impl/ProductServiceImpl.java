package net.provera.productserv.service.impl;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.provera.productserv.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import net.provera.productserv.dao.ProductRepository;
import net.provera.productserv.dao.model.Product;
import net.provera.productserv.dto.ProductDTO;
import net.provera.productserv.mapper.ProductMapper;
import net.provera.productserv.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final RedisTemplate<String, Object> redisTemplate;

	private ValueOperations<String, Object> getRedisValueOperations() {
		return redisTemplate.opsForValue();
	}
	private <T> T getFromCache(String key, TypeReference<T> valueType) {
		Object value = getRedisValueOperations().get(key);
		if (value != null) {
			ObjectMapper mapper = new ObjectMapper();
			if (value instanceof String) {
				try {
					return mapper.readValue((String) value, valueType);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Failed to parse cached value for key " + key, e);
				}
			} else if (value instanceof ArrayList) {
				try {
					String valueAsString = mapper.writeValueAsString(value);
					return mapper.readValue(valueAsString, valueType);
				} catch (JsonProcessingException e) {
					throw new RuntimeException("Failed to parse cached value for key " + key, e);
				}
			}
		}
		return null;
	}

	private <T> void setCache(String key, T value) {
		getRedisValueOperations().set(key, value);
	}

	private void invalidateCache(String... keys) {
		redisTemplate.delete(Arrays.asList(keys));
	}

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		Product product = productMapper.toProduct(productDTO);
		product = productRepository.save(product);

		// Invalidate the "all_products" cache
		invalidateCache("all_products");

		return productMapper.toProductDTO(product);
	}
	@Override
	public ProductDTO getProductById(String id) {
		ProductDTO productDTO = getFromCache(id, new TypeReference<ProductDTO>() {});

		if (productDTO != null) {
			return productDTO;
		}

		Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		productDTO = productMapper.toProductDTO(product);

		setCache(id, productDTO);

		return productDTO;
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		List<ProductDTO> productDTOs = getFromCache("all_products", new TypeReference<List<ProductDTO>>() {});

		if (productDTOs != null) {
			return productDTOs;
		}

		List<Product> products = productRepository.findAll();
		productDTOs = productMapper.toProductDTOs(products);

		setCache("all_products", productDTOs);

		return productDTOs;
	}

	@Override
	public List<ProductDTO> getProductsByCategoryId(String categoryId) {
		List<ProductDTO> productDTOs = getFromCache("products_by_category_id_" + categoryId, new TypeReference<List<ProductDTO>>() {});

		if (productDTOs != null) {
			return productDTOs;
		}

		List<Product> products = productRepository.findByCategoryId(categoryId);
		productDTOs = productMapper.toProductDTOs(products);

		setCache("products_by_category_id_" + categoryId, productDTOs);

		return productDTOs;
	}

	@Override
	public ProductDTO updateProduct(String id, ProductDTO productDTO) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product = productRepository.save(product);

		// Invalidate the caches for this product and "all_products"
		invalidateCache(id, "all_products");

		return productMapper.toProductDTO(product);
	}
	@Override
	public void deleteProduct(String id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		productRepository.delete(product);

		// Invalidate the caches for this product and "all_products"
		invalidateCache(id, "all_products");
	}

}