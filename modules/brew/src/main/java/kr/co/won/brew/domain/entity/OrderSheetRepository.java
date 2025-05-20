package kr.co.won.brew.domain.entity;


import kr.co.won.brew.domain.OrderId;

import java.util.Optional;

/**
 * @author springrunner.kr@gmail.com
 */
public interface OrderSheetRepository {
    Optional<OrderSheet> findByOrderId(OrderId orderId);
    OrderSheet save(OrderSheet orderSheet);
}
