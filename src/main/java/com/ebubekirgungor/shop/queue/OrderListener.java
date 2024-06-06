package com.ebubekirgungor.shop.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ebubekirgungor.shop.model.Order;
import com.ebubekirgungor.shop.model.User;
import com.ebubekirgungor.shop.repository.OrderRepository;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.github.sonus21.rqueue.annotation.RqueueListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class OrderListener {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderQueue {
        private Order order;
        private long userId;
    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @RqueueListener(value = "${order.queue.name}", numRetries = "3", deadLetterQueue = "failed-job-queue", concurrency = "5-10")
    public void onOrder(OrderQueue orderQueue) {
        Order order = orderQueue.getOrder();

        User user = userRepository.findById(orderQueue.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        order.setUser(user);

        orderRepository.save(order);
    }
}
