package kr.co.won.order.web;


import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.service.BrewOrderCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/order/brew-completed")
public class OrderBrewCompletedNotifyController {

    private final BrewOrderCompleted brewOrderCompleted;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public OrderBrewCompletedNotifyController(BrewOrderCompleted brewOrderCompleted) {
        this.brewOrderCompleted = Objects.requireNonNull(brewOrderCompleted, "OrderBrewComplete must not be null");
    }

    @PostMapping
    public ResponseEntity<Void> changeToCompleteStatus(@RequestBody UserBrewCompleteCommand command) {
        logger.info("Receive command requests: {}", command);

        // ----------------------------------------------------------
        // Validate order information and submit order-sheet
        // ----------------------------------------------------------

        brewOrderCompleted.changeOrderStatus(command.orderId());

        return ResponseEntity.accepted().build();
    }

    public record UserBrewCompleteCommand(OrderId orderId) {
    }
}
