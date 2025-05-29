package kr.co.won.user.data.message.dto;

import kr.co.won.user.domain.OrderId;

public record BrewCompletedEvent(OrderId orderId) {
}
