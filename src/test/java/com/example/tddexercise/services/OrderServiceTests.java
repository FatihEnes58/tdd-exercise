package com.example.tddexercise.services;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.tddexercise.clients.PaymentClient;
import com.example.tddexercise.dtos.OrderDto;
import com.example.tddexercise.models.Order;
import com.example.tddexercise.repositories.OrderRepository;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

  @InjectMocks //Mock olarak işaretlenmiş bağımlılıkların inject edilmesini sağlar
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private PaymentClient paymentClient;

  public static Stream<Arguments> createOrderRequestProvider() {

    return Stream.of(
        Arguments.of("code1", 5, 10, BigDecimal.valueOf(50)),
        Arguments.of("code2", 10, 15, BigDecimal.valueOf(150))
    );

  }

  @ParameterizedTest
  @MethodSource("createOrderRequestProvider")
  public void itShouldCreateOrders(String productCode, int amount, int unitPrice,
      BigDecimal totalPrice) {
    // given
    CreateOrderRequest request = CreateOrderRequest.builder()
        .productCode(productCode)
        .amount(amount)
        .unitPrice(BigDecimal.valueOf(unitPrice))
        .build();

    Order order = new Order();
    order.setId(58L);
    when(orderRepository.save(any())).thenReturn(order); //order repository üzerindeki save metodunu çağırınca order dönmesini bekliyoruz, dönüp dönmediği bu testin mevzusu değil.

    // when
    OrderDto orderDto = orderService.createOrder(request);
    // then
    then(orderDto).isNotNull();
    then(orderDto.getTotalPrice()).isEqualTo(totalPrice);
  }

  @Test
  public void itShouldFailOrderCreationWhenPaymentFailed() {
    // given
    CreateOrderRequest request = CreateOrderRequest.builder()
        .productCode("code1")
        .unitPrice(BigDecimal.valueOf(12))
        .amount(3)
        .build();

    doThrow(new IllegalArgumentException()).when(paymentClient).pay(any());

    // when
    Throwable throwable = catchThrowable(() -> orderService.createOrder(request));

    // then
    then(throwable).isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(orderRepository);

  }

}
