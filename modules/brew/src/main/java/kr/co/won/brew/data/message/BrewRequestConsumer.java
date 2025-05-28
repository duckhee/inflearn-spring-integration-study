package kr.co.won.brew.data.message;

import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.service.OrderSheetSubmission;
import kr.co.won.order.domain.message.BrewRequestCommand;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class BrewRequestConsumer {

    private final OrderSheetSubmission orderSheetSubmission;

    public BrewRequestConsumer(OrderSheetSubmission orderSheetSubmission) {
        this.orderSheetSubmission = orderSheetSubmission;
    }

    @ServiceActivator(inputChannel = "brewRequestChannel") // 메시지를 처리를 하기 위한 Annotation어느 채널에 해당 값들을 넣어줄지 결정한다. inputChannel로 설정된 channel에 해당 메시지를 전달한다.
    public void handle(BrewRequestCommand brewRequestCommand) {
        var command = brewRequestCommand;
        var brewOrderId = new OrderId(command.orderId().value());
        orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewOrderId));
    }
}
