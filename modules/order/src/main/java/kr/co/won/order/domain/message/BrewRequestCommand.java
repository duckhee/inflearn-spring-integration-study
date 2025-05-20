package kr.co.won.order.domain.message;


import kr.co.won.order.domain.OrderId;

/**
 * 전달할 Message에 대한 객체
 *
 * @param orderId
 */
public record BrewRequestCommand(OrderId orderId) {
}
