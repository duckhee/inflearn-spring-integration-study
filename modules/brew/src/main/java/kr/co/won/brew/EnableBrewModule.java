package kr.co.won.brew;

import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.service.OrderSheetSubmission;
import kr.co.won.order.domain.message.BrewRequestCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableBrewModule.BrewModuleConfiguration.class)
public @interface EnableBrewModule {

    @Configuration
    @ComponentScan
    class BrewModuleConfiguration {

        /**
         * Message Channel 에서 전달 받은 메시지에 대한 처리를 하는
         * @param orderSheetSubmission
         * @return
         */
        @Bean
        public MessageHandler messageHandler(OrderSheetSubmission orderSheetSubmission) {
            var handler = new MessageHandler() {

                @Override
                public void handleMessage(Message<?> message) throws MessagingException {
                    var command = (BrewRequestCommand) message.getPayload();
                    var  brewOrderId = new OrderId(command.orderId().value());
                    orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewOrderId));
                }
            };
            return handler;

        }
    }
}
