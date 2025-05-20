package kr.co.won.user.domain.service;

import kr.co.won.user.domain.OrderId;

@FunctionalInterface
public interface UserBrewCompleted {

    void notify(OrderId orderId);
}
