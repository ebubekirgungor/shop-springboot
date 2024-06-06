package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.repository.OrderRepository;
import com.ebubekirgungor.shop.repository.ProductRepository;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer;

import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Order;
import com.ebubekirgungor.shop.model.Order.DeliveryStatus;
import com.ebubekirgungor.shop.model.Order.OrderDTO;
import com.ebubekirgungor.shop.model.Order.OrderProduct;
import com.ebubekirgungor.shop.model.User.Cart;
import com.ebubekirgungor.shop.queue.OrderListener.OrderQueue;
import com.ebubekirgungor.shop.model.Product;
import com.ebubekirgungor.shop.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${origin-url}")
@RestController
@RequestMapping("${api-base}/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RqueueMessageEnqueuer rqueueMessageEnqueuer;

    @Value("${order.queue.name}")
    private String orderQueueName;

    @GetMapping
    public List<Order> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return orderRepository.findByUserId(((User) authentication.getPrincipal()).getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not exist with id :" + id));

        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Cart> cart = currentUser.getCart();

        Map<Long, Byte> ids = new HashMap<>();

        for (Cart item : cart) {
            if (item.getSelected()) {
                ids.put(item.getId(), item.getQuantity());
            }
        }

        double total_amount = 0;

        List<Product> products = productRepository.findAllById(ids.keySet());

        List<OrderProduct> orderProducts = new ArrayList<>();

        for (Product product : products) {
            orderProducts.add(new OrderProduct(product.getUrl(), product.getTitle(), product.getList_price(),
                    product.getImages().get(0).getName(), ids.get(product.getId())));

            total_amount += product.getList_price() * ids.get(product.getId());
        }

        Order order = new Order();
        order.setCustomer_name(orderDTO.getCustomer_name());
        order.setDelivery_address(orderDTO.getDelivery_address());
        order.setTotal_amount(total_amount);
        order.setDelivery_status(DeliveryStatus.InProgress.getValue());
        order.setProducts(orderProducts);

        OrderQueue orderQueue = new OrderQueue(order, currentUser.getId());

        rqueueMessageEnqueuer.enqueue(orderQueueName, orderQueue);

        List<Cart> newCart = new ArrayList<>();

        for (Cart item : cart) {
            if (!item.getSelected()) {
                newCart.add(item);
            }
        }

        currentUser.setCart(newCart);
        userRepository.save(currentUser);

        return ResponseEntity.ok("ok");
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
