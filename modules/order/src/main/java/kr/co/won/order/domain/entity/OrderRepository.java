package kr.co.won.order.domain.entity;


import kr.co.won.order.domain.OrderId;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(OrderId id);
    Order save(Order order);
}
