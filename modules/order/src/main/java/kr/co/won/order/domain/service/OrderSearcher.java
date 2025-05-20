package kr.co.won.order.domain.service;


import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.entity.Order;

@FunctionalInterface
public interface OrderSearcher {
    Order findById(OrderId orderId);
}
