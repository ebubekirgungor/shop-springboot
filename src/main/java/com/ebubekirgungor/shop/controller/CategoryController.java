package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.repository.CategoryRepository;
import com.ebubekirgungor.shop.request.CategoryRequest;
import com.ebubekirgungor.shop.response.CategoryResponse;
import com.ebubekirgungor.shop.service.UploadService;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Category;
import com.ebubekirgungor.shop.model.Category.CategoryDTO;
import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.Product.ProductFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${origin-url}", allowCredentials = "true")
@RestController
@RequestMapping("${api-base}/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UploadService uploadService;

    @Cacheable(value = "categoriesCache")
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @CacheEvict(value = "categoriesCache", allEntries = true)
    @PostMapping
    public ResponseEntity<String> createCategory(@ModelAttribute CategoryDTO categoryDTO) {

        try {
            uploadService.upload(categoryDTO.getImage(), "images/categories");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not upload the image");
        }

        Category category = new Category();
        category.setTitle(categoryDTO.getTitle());
        category.setUrl(categoryDTO.getUrl());
        category.setImage(categoryDTO.getImage().getOriginalFilename());
        category.setFilters(categoryDTO.getFilters());

        categoryRepository.save(category);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{url}")
    public ResponseEntity<CategoryResponse> getCategoryByUrl(@PathVariable String url,
            @RequestBody(required = false) CategoryRequest categoryRequest) {

        Category category = categoryRepository.findByUrl(url)
                .orElseThrow(() -> new ResourceNotFoundException("Category not exist with url :" + url));

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setTitle(category.getTitle());
        categoryResponse.setFilters(category.getFilters());

        if (categoryRequest == null) {
            categoryResponse.setProducts(category.getProducts());
            return ResponseEntity.ok(categoryResponse);
        }

        Map<String, List<String>> categoryFilters = categoryRequest.getFilters();

        List<Product> allProducts = new ArrayList<>();

        for (Product product : category.getProducts()) {
            boolean add = false;

            for (ProductFilter filter : product.getFilters()) {
                if (categoryFilters.get(filter.getName()).contains(filter.getValue())) {
                    add = true;
                }
            }

            if (add) {
                allProducts.add(product);
            }
        }

        categoryResponse.setProducts(allProducts);

        return ResponseEntity.ok(categoryResponse);
    }

    @CacheEvict(value = "categoriesCache", allEntries = true)
    @PutMapping("/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable Long id) {

        Category oldCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not exist with id :" + id));

        oldCategory.setTitle(category.getTitle());
        oldCategory.setUrl(category.getUrl());
        oldCategory.setImage(category.getImage());
        oldCategory.setFilters(category.getFilters());

        return categoryRepository.save(oldCategory);
    }

    @CacheEvict(value = "categoriesCache", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not exist with id :" + id));

        categoryRepository.delete(category);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
