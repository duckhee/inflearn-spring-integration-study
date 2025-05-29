package kr.co.won.user.data.message;

import kr.co.won.user.data.message.dto.BrewCompletedEvent;
import kr.co.won.user.domain.OrderId;
import kr.co.won.user.domain.service.UserBrewCompleted;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * 제조 완료 시 발생하는 이벤트에 대해서 구독해서 처리를 하는 클래스
 */
@Component
public class UserBrewCompletedEventListener {

    private final UserBrewCompleted userBrewCompleted;

    public UserBrewCompletedEventListener(UserBrewCompleted userBrewCompleted) {
        this.userBrewCompleted = userBrewCompleted;
    }

    @ServiceActivator(inputChannel = "brewCompletedEventPublishSubscribeChannel")
    public void handle(BrewCompletedEvent brewCompletedEvent) {

        userBrewCompleted.notify(brewCompletedEvent.orderId());

    }
}
