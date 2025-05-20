package kr.co.won.order.domain.service.business;

import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.entity.OrderNotFoundException;
import kr.co.won.order.domain.entity.OrderRepository;
import kr.co.won.order.domain.service.BarCounter;
import kr.co.won.order.domain.service.BrewOrderCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BrewOrderCompletedProcessor implements BrewOrderCompleted {
    private final OrderRepository orderRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BrewOrderCompletedProcessor(OrderRepository orderRepository, BarCounter barCounter) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    @Override
    public void changeOrderStatus(OrderId orderId) {
        logger.info("Change order-status: %s".formatted(orderId));
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.complete();
        orderRepository.save(order);
    }
}
