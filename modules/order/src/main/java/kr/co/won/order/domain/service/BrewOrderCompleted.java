package kr.co.won.order.domain.service;


import kr.co.won.order.domain.OrderId;

@FunctionalInterface
public interface BrewOrderCompleted {

    void changeOrderStatus(OrderId orderId);
}
