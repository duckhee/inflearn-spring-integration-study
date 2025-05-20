package kr.co.won.order.data.repository;


import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.entity.Order;
import kr.co.won.order.domain.entity.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public Optional<Order> findById(OrderId id) {
        return orders.stream().filter(order -> order.getId().equals(id)).findFirst();
    }

    @Override
    public Order save(Order order) {
        orders.add(order);
        return order;
    }
}
