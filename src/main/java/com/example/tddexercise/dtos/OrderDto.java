package com.example.tddexercise.dtos;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

  private BigDecimal totalPrice;
  private Long id;
}
