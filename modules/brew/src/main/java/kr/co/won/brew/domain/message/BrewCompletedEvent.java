package kr.co.won.brew.domain.message;

import kr.co.won.brew.domain.OrderId;

/**
 * 제조 요청은 이미 발생한 액션이기 때문에 이벤트에 해당이 된다.
 * 변경이 불가능하기 때문에 Event이다.
 *
 * @param orderId
 */
public record BrewCompletedEvent(OrderId orderId) {
}
