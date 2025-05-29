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
//@Primary
@Component
public class HttpNotificationMessagePublisher implements BrewNotifier {

    ///  spring에서 정의된 이름을 가지고 분리를 하기 때문에 이름이 길어진다.
    private final MessageChannel brewCompletedNotifyOrderChannel;
    /// 사용자에게 알려주기 위한 MessageChannel
    private final MessageChannel brewCompletedNotifyUserChannel;

    public HttpNotificationMessagePublisher(MessageChannel brewCompletedNotifyOrderChannel, MessageChannel brewCompletedNotifyUserChannel) {
        this.brewCompletedNotifyOrderChannel = brewCompletedNotifyOrderChannel;
        this.brewCompletedNotifyUserChannel = brewCompletedNotifyUserChannel;
    }


    @Override
    public void notify(OrderId orderId) {
        BrewCompletedEvent brewCompletedEvent = new BrewCompletedEvent(orderId);
        /// message로 전송을 하기 위해서 GenericMessage로 만들어준다.
        GenericMessage<BrewCompletedEvent> brewCompletedEventGenericMessage = new GenericMessage<>(brewCompletedEvent);
        brewCompletedNotifyOrderChannel.send(brewCompletedEventGenericMessage);
        brewCompletedNotifyUserChannel.send(brewCompletedEventGenericMessage);
    }
}
