package kr.co.won.order.domain.service;


import kr.co.won.order.domain.OrderId;

@FunctionalInterface
public interface BarCounter {

    /**
     * Request to brew a drink.
     *
     * @param orderId The ID of the order
     */
    void brew(OrderId orderId);
}
