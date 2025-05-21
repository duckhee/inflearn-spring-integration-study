package kr.co.won.coffeehose.libraries.message;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Observable;

/**
 * Observable은 java9에서 deprecated 되었다.
 */
public class ObservableChannel extends Observable implements MessageChannel {
    @Override
    public boolean send(Message<?> message) {
        ///  현재 전달할 사항이 있다고 변경을 해주는 함수 호출
        this.setChanged();
        ///  해당 객체를 관찰하도록 설정이 되어 있는 관찰자들에게 메세지를 전달을 해준다.
        this.notifyObservers(message);
        return MessageChannel.super.send(message);
    }

    @Override
    public boolean send(Message<?> message, long timeout) {
        return false;
    }
}
