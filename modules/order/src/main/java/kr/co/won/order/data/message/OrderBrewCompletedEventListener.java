package kr.co.won.order.data.message;

import kr.co.won.order.domain.service.BrewOrderCompleted;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import kr.co.won.order.data.message.dto.BrewCompletedEvent;

/**
 * messageChannel에서 온 메시지를 처리하는 class
 */
@Component
public class OrderBrewCompletedEventListener {

    private final BrewOrderCompleted brewOrderCompleted;

    public OrderBrewCompletedEventListener(BrewOrderCompleted brewOrderCompleted) {
        this.brewOrderCompleted = brewOrderCompleted;
    }


    /**
     * 해당 채널을 구독을 통해서 해당 메시지가 전달이 되었을 때 처리를 하는 함수
     *
     * @param brewCompletedEvent
     */
    @ServiceActivator(inputChannel = "brewCompletedEventPublishSubscribeChannel")
    //  비즈니스 로직과 메시지 채널을 연결 시켜주기 위한 @ServiceActivator 정의
    public void handle(BrewCompletedEvent brewCompletedEvent) {
        brewOrderCompleted.changeOrderStatus(brewCompletedEvent.orderId());
    }
}
