package com.db.services;
import com.db.entities.Order;

import java.util.Optional;

public interface OrderService {

    Order save(Order order);
    Iterable<Order> findAll();

    Optional<Order> findById(Long id);
}
