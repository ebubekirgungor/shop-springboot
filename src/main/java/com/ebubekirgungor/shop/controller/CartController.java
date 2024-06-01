package com.ebubekirgungor.shop.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.model.User.Cart;
import com.ebubekirgungor.shop.repository.ProductRepository;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.ebubekirgungor.shop.response.CartResponse;

@CrossOrigin(origins = "${origin-url}")
@RestController
@RequestMapping("${api-base}/users/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @GetMapping
    public ResponseEntity<List<CartResponse>> getCart() {
        User currentUser = (User) authentication.getPrincipal();
        final Cart[] cart = currentUser.getCart();

        List<Long> ids = new ArrayList<>();

        for (Cart item : cart) {
            ids.add(item.getId());
        }

        List<CartResponse> products = productRepository.findAllById(ids).stream().map(e -> (CartResponse) e)
                .collect(Collectors.toList());

        for (int i = 0; i < products.size(); i++) {
            products.get(i).setCart_quantity(cart[i].getQuantity());
            products.get(i).setSelected(cart[i].getSelected());
        }

        return ResponseEntity.ok(products);
    }

    @PatchMapping
    public ResponseEntity<String> updateCart(@RequestBody User user) {
        User currentUser = (User) authentication.getPrincipal();
        currentUser.setCart(user.getCart());

        userRepository.save(currentUser);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long id) {
        User currentUser = (User) authentication.getPrincipal();
        Cart[] cart = currentUser.getCart();

        for (int i = 0; i < cart.length; i++) {
            if (cart[i].getId() == id) {
                cart[i].setQuantity((byte) (cart[i].getQuantity() + 1));
                userRepository.save(currentUser);
                return ResponseEntity.ok("ok");
            }
        }

        Cart newCart = currentUser.new Cart(id, (byte) 1, true);
        cart = Arrays.copyOf(cart, cart.length + 1);
        cart[cart.length - 1] = newCart;

        userRepository.save(currentUser);

        return ResponseEntity.ok("ok");
    }
}
