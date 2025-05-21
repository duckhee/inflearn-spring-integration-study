package kr.co.won.brew.domain.entity;


import kr.co.won.brew.domain.OrderId;

import java.util.Optional;

public interface OrderSheetRepository {
    Optional<OrderSheet> findByOrderId(OrderId orderId);
    OrderSheet save(OrderSheet orderSheet);
}
