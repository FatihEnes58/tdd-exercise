package com.example.tddexercise.repositories;

import com.example.tddexercise.models.Order;
import org.springframework.data.repository.CrudRepository;


public interface OrderRepository extends CrudRepository<Order, Long> {}
