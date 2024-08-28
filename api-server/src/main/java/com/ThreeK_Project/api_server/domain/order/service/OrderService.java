package com.ThreeK_Project.api_server.domain.order.service;

import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.repository.OrderRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponseDto getOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        return new OrderResponseDto(order);
    }

    public void deleteOrder(UUID orderId, User user) {
        Order order = findOrderById(orderId);
        order.deleteBy(user);
        orderRepository.save(order);
    }

    public Order findOrderById(UUID orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("Order not found"));
    }
}
