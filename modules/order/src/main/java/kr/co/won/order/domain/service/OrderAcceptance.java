package kr.co.won.order.domain.service;


import kr.co.won.order.domain.OrderId;

@FunctionalInterface
public interface OrderAcceptance {

    /**
     * Accepts a received order.
     *
     * @param orderId The ID of the order to accept
     */
    void acceptOrder(OrderId orderId);
}
