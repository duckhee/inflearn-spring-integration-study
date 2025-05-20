package kr.co.won.brew.domain.service.business;


import kr.co.won.brew.domain.OrderSheetId;
import kr.co.won.brew.domain.entity.OrderSheet;
import kr.co.won.brew.domain.entity.OrderSheetRepository;
import kr.co.won.brew.domain.service.OrderSheetSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author springrunner.kr@gmail.com
 */
@Service
class BrewingProcessor implements OrderSheetSubmission {

    private final OrderSheetRepository orderSheetRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    BrewingProcessor(OrderSheetRepository orderSheetRepository) {
        this.orderSheetRepository = orderSheetRepository;
    }

    @Override
    public void submit(OrderSheetForm orderSheetForm) {
        logger.info("Submitted order-sheet: %s".formatted(orderSheetForm));
        var orderSheetId = new OrderSheetId(UUID.randomUUID().toString());
        var orderSheet = OrderSheet.create(orderSheetId, orderSheetForm.orderId());
        orderSheetRepository.save(orderSheet);
    }
}
