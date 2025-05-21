package kr.co.won.order.data.message;


import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.message.BrewRequestCommand;
import kr.co.won.order.domain.service.BarCounter;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * 제조 요청에 대한 메시지 처리 컴포넌트
 */
@Primary
@Component
public class BrewRequestProducer implements BarCounter {

    ///  추후 MessageChannel에 대한 빈으로 등록을 해준다.
    private final MessageChannel barCounterChannel;

    public BrewRequestProducer(MessageChannel barCounterChannel) {
        this.barCounterChannel = barCounterChannel;
    }

    @Override
    public void brew(OrderId orderId) {
        var command = new BrewRequestCommand(orderId);

        ///  spring message는 Message로 감싸주기 위해서 제공이 되는 두 가지 객체를 제공을 한다.
        // GenericMessage
        var message = new GenericMessage<BrewRequestCommand>(command);

        // MessageBuilder
//        var message = MessageBuilder.withPayload(command).build();

        ///  message Channel에 메시지를 전달을 하는 함수이다.
        barCounterChannel.send(message);
    }
}
