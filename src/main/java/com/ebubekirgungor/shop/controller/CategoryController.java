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
import com.ebubekirgungor.shop.request.CategoryRequest;
import com.ebubekirgungor.shop.response.CategoryResponse;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Category;
import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.Product.ProductFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${origin-url}")
@RestController
@RequestMapping("${api-base}/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/{url}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String url,
            @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByUrl(url)
                .orElseThrow(() -> new ResourceNotFoundException("Category not exist with url :" + url));

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

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setTitle(category.getTitle());
        categoryResponse.setFilters(category.getFilters());
        categoryResponse.setProducts(allProducts);

        return ResponseEntity.ok(categoryResponse);
    }

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
