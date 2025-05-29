package kr.co.won.order.data.message.dto;

import kr.co.won.order.domain.OrderId;

public record BrewCompletedEvent(OrderId orderId) {
}
