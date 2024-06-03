package com.ebubekirgungor.shop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.repository.ProductRepository;

@CrossOrigin(origins = "${origin-url}")
@RestController
@RequestMapping("${api-base}/favorites")
public class FavoritesController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllFavorites() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return productRepository.findProductsByUsersId(((User) authentication.getPrincipal()).getId());
    }

    @GetMapping("/ids")
    public List<Long> getFavoritesIds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Long> ids = new ArrayList<Long>();

        for (Product product : productRepository
                .findProductsByUsersId(((User) authentication.getPrincipal()).getId())) {
            ids.add(product.getId());
        }

        return ids;
    }

    @PostMapping("/{product_id}")
    public ResponseEntity<String> addProductToFavorites(@PathVariable(name = "product_id") Long product_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not exist with id :" + product_id));

        List<User> users = new ArrayList<User>();
        users.add(currentUser);
        product.setUsers(users);

        productRepository.save(product);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable(name = "product_id") Long product_id) {
        productRepository.deleteById(product_id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
