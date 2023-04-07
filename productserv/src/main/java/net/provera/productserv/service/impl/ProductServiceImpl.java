package net.provera.productserv.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import net.provera.productserv.dao.ProductRepository;
import net.provera.productserv.dao.model.Product;
import net.provera.productserv.dto.ProductDTO;
import net.provera.productserv.exception.ResourceNotFoundException;
import net.provera.productserv.mapper.ProductMapper;
import net.provera.productserv.service.ProductService;

@Service
@CacheConfig(cacheNames = "products")
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {
		Product product = productMapper.toProduct(productDTO);
		Product savedProduct = productRepository.save(product);
		return productMapper.toProductDTO(savedProduct);
	}

	@Override
	@Cacheable(value = "products", key = "'all'")
	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll().stream()
				.map(productMapper::toProductDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Cacheable(value = "products", key = "#categoryId")
	public List<ProductDTO> getProductsByCategoryId(String categoryId) {
		return productRepository.findByCategoryId(categoryId).stream()
				.map(productMapper::toProductDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Cacheable(value = "products", key = "#id")
	public ProductDTO getProductById(String id) {
		Optional<Product> productOptional = productRepository.findById(id);
		return productOptional.map(productMapper::toProductDTO)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
	}

	@Override
	@CachePut(key = "#id")
	public ProductDTO updateProduct(String id, ProductDTO productDTO) {
		Product product = getProductByIdIfExists(id);
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setCategoryId(productDTO.getCategoryId());
		Product updatedProduct = productRepository.save(product);
		return productMapper.toProductDTO(updatedProduct);
	}

	@Override
	public void deleteProduct(String id) {
		Product product = getProductByIdIfExists(id);
		productRepository.delete(product);
	}

	private Product getProductByIdIfExists(String id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

}