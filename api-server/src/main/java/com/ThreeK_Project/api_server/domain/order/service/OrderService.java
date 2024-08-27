package com.ThreeK_Project.api_server.domain.order.service;

import com.ThreeK_Project.api_server.domain.order.dto.OrderRequestDto;
import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.dto.OrderedProduct;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.repository.OrderProductRepository;
import com.ThreeK_Project.api_server.domain.order.repository.OrderRepository;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.repository.ProductRepository;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void createOrder(OrderRequestDto requestDto) {

        if(requestDto.getProductList().isEmpty())
            throw new ApplicationException("Product list is empty");

        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() ->  new ApplicationException("Restaurant not found"));

        Order order = Order.createOrder(
                requestDto.getOrderType(), OrderStatus.WAIT, requestDto.getOrderAmount(),
                requestDto.getDeliveryAddress(), requestDto.getRequestDetails(), restaurant
        );
        final Order savedOrder = orderRepository.save(order);

        for(OrderedProduct orderedProduct: requestDto.getProductList()){
            int quantity = orderedProduct.getQuantity();

            Product product = productRepository.findById(orderedProduct.getProductId())
                    .orElseThrow(() -> new ApplicationException("Product not found"));

            OrderProduct orderProduct = OrderProduct.createOrderProduct(quantity, new BigDecimal(product.getPrice() * quantity), savedOrder, product);
            orderProductRepository.save(orderProduct);
        }

    }

    public OrderResponseDto getOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("Order not found"));

        return new OrderResponseDto(order);
    }
}
