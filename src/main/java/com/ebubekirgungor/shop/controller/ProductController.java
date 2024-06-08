package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.repository.CategoryRepository;
import com.ebubekirgungor.shop.repository.ProductRepository;
import com.ebubekirgungor.shop.response.ProductResponse;
import com.ebubekirgungor.shop.service.UploadService;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Category;
import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.Product.ProductDTO;
import com.ebubekirgungor.shop.model.Product.ProductImage;
import com.ebubekirgungor.shop.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin(origins = "${origin-url}")
@RestController
@RequestMapping("${api-base}/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UploadService uploadService;

	@GetMapping
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<String> createProduct(@ModelAttribute ProductDTO productDTO) {

		List<ProductImage> imageNames = new ArrayList<>();
		try {
			AtomicInteger index = new AtomicInteger(0);
			Arrays.asList(productDTO.getImages()).stream().forEach(image -> {
				uploadService.upload(image, "images/products");
				imageNames.add(new ProductImage(image.getOriginalFilename(), (byte) index.getAndIncrement()));
			});
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not upload the images");
		}

		Product product = new Product();
		product.setTitle(productDTO.getTitle());
		product.setUrl(productDTO.getUrl());
		product.setList_price(productDTO.getList_price());
		product.setStock_quantity(productDTO.getStock_quantity());
		product.setImages(imageNames);
		product.setFilters(productDTO.getFilters());

		Category category = categoryRepository.findById(productDTO.getCategory_id())
				.orElseThrow(() -> new RuntimeException("Category not found"));
		product.setCategory(category);

		productRepository.save(product);

		return ResponseEntity.ok("ok");
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not exist with id :" + id));

		boolean is_favorite = false;

		for (Product item : productRepository.findProductsByUsersId(((User) authentication.getPrincipal()).getId())) {
			if (item.getId() == id) {
				is_favorite = true;
			}
		}

		return ResponseEntity.ok(new ProductResponse(product.getId(), product.getTitle(), product.getUrl(),
				product.getList_price(), product.getStock_quantity(), product.getImages(), product.getFilters(),
				product.getCategoryTitle(), is_favorite));
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
