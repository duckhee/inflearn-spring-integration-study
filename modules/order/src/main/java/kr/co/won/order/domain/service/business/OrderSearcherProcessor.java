package kr.co.won.order.domain.service.business;


import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.entity.Order;
import kr.co.won.order.domain.entity.OrderNotFoundException;
import kr.co.won.order.domain.entity.OrderRepository;
import kr.co.won.order.domain.service.OrderSearcher;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderSearcherProcessor implements OrderSearcher {
    private final OrderRepository orderRepository;

    public OrderSearcherProcessor(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public Order findById(OrderId orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }
}
