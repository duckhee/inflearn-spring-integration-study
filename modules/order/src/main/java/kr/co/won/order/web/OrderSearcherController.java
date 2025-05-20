package kr.co.won.order.web;

import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.entity.Order;
import kr.co.won.order.domain.service.OrderSearcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/order/{orderId}")
public class OrderSearcherController {

    private final OrderSearcher orderSearcher;

    public OrderSearcherController(OrderSearcher orderSearcher) {
        this.orderSearcher = Objects.requireNonNull(orderSearcher, "OrderSearcher must not be null");
    }

    @GetMapping
    public ResponseEntity<Order> searchOrder(@PathVariable String orderId) {
        var order = orderSearcher.findById(new OrderId(orderId));
        return ResponseEntity.ok(order);
    }
}
