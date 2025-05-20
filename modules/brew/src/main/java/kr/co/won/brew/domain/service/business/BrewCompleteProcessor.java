package kr.co.won.brew.domain.service.business;


import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.entity.OrderSheetNotFoundException;
import kr.co.won.brew.domain.entity.OrderSheetRepository;
import kr.co.won.brew.domain.service.BrewComplete;
import kr.co.won.brew.domain.service.BrewNotifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
public class BrewCompleteProcessor implements BrewComplete {

    private final OrderSheetRepository orderSheetRepository;
    private final BrewNotifier brewNotifier;

    public BrewCompleteProcessor(BrewNotifier brewNotifier, OrderSheetRepository orderSheetRepository) {
        this.orderSheetRepository = Objects.requireNonNull(orderSheetRepository, "OrderSheetRepository must not be null");
        this.brewNotifier = Objects.requireNonNull(brewNotifier, "BrewNotifier must not be null");
    }

    @Override
    public void complete(OrderId orderId) {
        // ----------------------------------------------------------
        // Verify brew information with the order iD and completed it
        // ----------------------------------------------------------
        var orderSheet = orderSheetRepository.findByOrderId(orderId).orElseThrow(OrderSheetNotFoundException::new);
        orderSheet.process();
        brewNotifier.notify(orderId);
    }
}
