package com.example.tddexercise.services;

import com.example.tddexercise.clients.PaymentClient;
import com.example.tddexercise.dtos.OrderDto;
import com.example.tddexercise.models.Order;
import com.example.tddexercise.repositories.OrderRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final PaymentClient paymentClient;

  public OrderService(OrderRepository orderRepository,
      PaymentClient paymentClient) {
    this.orderRepository = orderRepository;
    this.paymentClient = paymentClient;
  }

  public OrderDto createOrder(CreateOrderRequest request) {

    BigDecimal totalPrice = request.getUnitPrice()
        .multiply(BigDecimal.valueOf(request.getAmount()));
    Order order = Order.builder().totalPrice(totalPrice).build();
    paymentClient.pay(order);
    Order savedOrder = orderRepository.save(order);
    return OrderDto.builder().id(savedOrder.getId()).totalPrice(totalPrice).build();
  }


}
