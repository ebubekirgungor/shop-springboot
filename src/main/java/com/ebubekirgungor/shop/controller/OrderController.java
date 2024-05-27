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

import com.ebubekirgungor.shop.repository.OrderRepository;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Order;
import com.ebubekirgungor.shop.model.Order.OrderDTO;
import com.ebubekirgungor.shop.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = new Order();
        order.setTotal_amount(orderDTO.getTotal_amount());
        order.setCustomer_name(orderDTO.getCustomer_name());
        order.setDelivery_address(orderDTO.getDelivery_address());
        order.setDelivery_status(orderDTO.getDelivery_status().getValue());
        order.setProducts(orderDTO.getProducts());

        User user = userRepository.findById(orderDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        return orderRepository.save(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not exist with id :" + id));
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not exist with id :" + id));

        orderRepository.delete(order);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
