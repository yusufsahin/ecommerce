package net.provera.productserv.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.provera.productserv.service.ProductService;
import net.provera.productserv.dto.ProductDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;



@RestController
@RequestMapping("/api/products")

public class ProductController {

		@Autowired
		private final ProductService productService;

		@Autowired
	    public ProductController(ProductService productService) {
			this.productService = productService;
	    }


		@PostMapping
		public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
		ProductDTO savedProduct = productService.createProduct(productDTO);
		return ResponseEntity
				.created(URI.create("/api/products/" + savedProduct.getId()))
				.body(savedProduct);
		}


		@GetMapping("/category/{categoryId}")
		public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable String categoryId) {
			List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
			return ResponseEntity.ok(products);
		}

	    @GetMapping
	    public ResponseEntity<List<ProductDTO>> getAllProducts() {
	        List<ProductDTO> products = productService.getAllProducts();
			return ResponseEntity.ok(products);
		}

	    @GetMapping("/{id}")
	    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
	    	ProductDTO product = productService.getProductById(id);
	        return  ResponseEntity.ok(product);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDTO productDTO) {
	    	ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
	        return ResponseEntity.ok(updatedProduct);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
	    	productService.deleteProduct(id);
	        return ResponseEntity.noContent().build();
	    }


}
