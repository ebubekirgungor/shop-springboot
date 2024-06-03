package com.ebubekirgungor.shop.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.model.User.Cart;
import com.ebubekirgungor.shop.model.User.CartDTO;
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

    @GetMapping
    public List<CartResponse> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final List<Cart> cart = ((User) authentication.getPrincipal()).getCart();

        List<Long> ids = new ArrayList<>();

        for (Cart item : cart) {
            ids.add(item.getId());
        }

        List<Product> products = productRepository.findAllById(ids);
        List<CartResponse> cartResponse = new ArrayList<>();

        int index = 0;
        for (Product product : products) {
            cartResponse.add(
                    new CartResponse(product.getId(), product.getTitle(), product.getUrl(), product.getList_price(),
                            product.getStock_quantity(), product.getImages(), cart.get(index).getQuantity(),
                            cart.get(index).getSelected()));
            index++;
        }

        return cartResponse;
    }

    @PatchMapping
    public ResponseEntity<String> updateCart(@RequestBody CartDTO cartDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        currentUser.setCart(cartDTO.getCart());

        userRepository.save(currentUser);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Cart> cart = currentUser.getCart();

        for (Cart item : cart) {
            if (item.getId() == id) {
                item.setQuantity((byte) (item.getQuantity() + 1));
                userRepository.save(currentUser);
                return ResponseEntity.ok("ok");
            }
        }

        cart.add(new Cart(id, (byte) 1, true));

        currentUser.setCart(cart);

        userRepository.save(currentUser);

        return ResponseEntity.ok("ok");
    }
}
