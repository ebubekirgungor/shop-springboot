package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.repository.CategoryRepository;
import com.ebubekirgungor.shop.repository.ProductRepository;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Category;
import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.Product.ProductDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@PostMapping
	public Product createProduct(@RequestBody ProductDTO productDTO) {
		Product product = new Product();
		product.setTitle(productDTO.getTitle());
		product.setUrl(productDTO.getUrl());
		product.setList_price(productDTO.getList_price());
		product.setStock_quantity(productDTO.getStock_quantity());
		product.setImages(productDTO.getImages());
		product.setFilters(productDTO.getFilters());

		Category category = categoryRepository.findById(productDTO.getCategory_id())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		product.setCategory(category);

		return productRepository.save(product);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id :" + id));
		return ResponseEntity.ok(product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id :" + id));

		productRepository.delete(product);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
