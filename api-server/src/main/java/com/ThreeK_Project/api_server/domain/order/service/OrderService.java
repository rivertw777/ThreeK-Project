package com.ThreeK_Project.api_server.domain.order.service;

import com.ThreeK_Project.api_server.domain.order.dto.OrderRequestDto;
import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.dto.ProductRequestData;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.repository.OrderProductRepository;
import com.ThreeK_Project.api_server.domain.order.repository.OrderRepository;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.service.ProductService;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;


    public String createOrder(OrderRequestDto requestDto, Restaurant restaurant) {
        Order savedOrder = saveOrder(requestDto, restaurant);
        requestDto.getProductList()
                .forEach(productData -> {saveOrderProduct(productData, savedOrder);});

        return "주문 생성 성공";
    }

    public Order saveOrder(OrderRequestDto requestDto, Restaurant restaurant) {
        Order order = Order.createOrder(
                requestDto.getOrderType(), OrderStatus.WAIT, requestDto.getOrderAmount(),
                requestDto.getDeliveryAddress(), requestDto.getRequestDetails(), restaurant
        );
        return orderRepository.save(order);
    }

    public OrderProduct saveOrderProduct(ProductRequestData productData, Order order) {
        Product product = productService.getProductById(productData.getProductId());

        OrderProduct orderProduct = OrderProduct.createOrderProduct(
                productData.getQuantity(), productData.getProductAmount(), order, product
        );
        return orderProductRepository.save(orderProduct);
    }


    public OrderResponseDto getOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId, User user) {
        Order order = findOrderById(orderId);
        order.deleteBy(user);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId, String username) {
        LocalDateTime now = LocalDateTime.now();
        Order order = findOrderById(orderId);

        if(!order.getCreatedBy().getUsername().equals(username))
            throw new ApplicationException("Invalid user");

        if(order.getOrderStatus() != OrderStatus.WAIT)
            throw new ApplicationException("Cannot cancel");

        if(Duration.between(order.getCreatedAt(), now).toMinutes() >= 5)
            throw new ApplicationException("Cancel Timeout");

        order.changeStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus newOrderStatus) {
        if(newOrderStatus == OrderStatus.WAIT)
            throw new ApplicationException("Invalid order status");

        Order order = findOrderById(orderId);

        OrderStatus oldOrderStatus = order.getOrderStatus();
        if(oldOrderStatus == OrderStatus.WAIT && newOrderStatus == OrderStatus.CANCELED)
            order.changeStatus(OrderStatus.CANCELED);
        else if(oldOrderStatus == OrderStatus.WAIT && newOrderStatus == OrderStatus.RECEIPT)
            order.changeStatus(OrderStatus.RECEIPT);
        else if(oldOrderStatus == OrderStatus.RECEIPT && newOrderStatus == OrderStatus.COMPLETE)
            order.changeStatus(OrderStatus.COMPLETE);
        else
            throw new ApplicationException("Invalid order status");
        orderRepository.save(order);
    }

    public Order findOrderById(UUID orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("Order not found"));
    }

}