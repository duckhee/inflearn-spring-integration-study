package kr.co.won.brew.data.message;

import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.message.BrewCompletedEvent;
import kr.co.won.brew.domain.service.BrewNotifier;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * message를 이용한 Brew에 대한 알림 기능을 사용하는 객체이다.
 */
@Primary
@Component
public class AmqpNotificationMessagePublisher implements BrewNotifier {

    /**
     * 여러 채널로 전송을 하는 것을 하나의 채널로 묶어주기 위한 채널
     */
    private final MessageChannel brewCompletedChannel;

    public AmqpNotificationMessagePublisher(MessageChannel brewCompletedChannel) {
        this.brewCompletedChannel = brewCompletedChannel;
    }

    @Override
    public void notify(OrderId orderId) {
        BrewCompletedEvent brewCompletedEvent = new BrewCompletedEvent(orderId);
        GenericMessage<BrewCompletedEvent> brewCompletedEventGenericMessage = new GenericMessage<>(brewCompletedEvent);
        brewCompletedChannel.send(brewCompletedEventGenericMessage);
    }
}
