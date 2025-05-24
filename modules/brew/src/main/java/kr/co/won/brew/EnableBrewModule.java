package kr.co.won.brew;

import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.service.OrderSheetSubmission;
import kr.co.won.order.domain.message.BrewRequestCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Observable;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableBrewModule.BrewModuleConfiguration.class)
public @interface EnableBrewModule {

    @Configuration
    @ComponentScan
    class BrewModuleConfiguration {

        /**
         * Message Channel 에서 전달 받은 메시지에 대한 처리를 하는 Bean 설정
         *
         * @param orderSheetSubmission
         * @param barCounterChannel
         * @return
         */
//        @Bean
//        public MessageHandler messageHandler(OrderSheetSubmission orderSheetSubmission, MessageChannel barCounterChannel) {
//            var handler = new MessageHandler() {
//
//                @Override
//                public void handleMessage(Message<?> message) throws MessagingException {
//                    var command = (BrewRequestCommand) message.getPayload();
//                    var brewOrderId = new OrderId(command.orderId().value());
//                    orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewOrderId));
//                }
//            };
//
//            /** 관찰자로 등록을 하기 위한 객체 형변환 */
//            var observer = (Observable) barCounterChannel;
//            observer.addObserver((o, arg) -> {
//                var message = (Message<?>) arg;
//                handler.handleMessage(message);
//            });
//
//            return handler;
//        }

        /**
         * Spring-Integration을 이용한 Message Channel에 대한 처리를 하는 Bean 정의
         *
         * @param orderSheetSubmission
         * @param barCounterChannel
         * @return
         */
        @Bean
        public IntegrationFlow requestBrewIntegrationFlow(OrderSheetSubmission orderSheetSubmission, MessageChannel barCounterChannel) {
            return IntegrationFlow.from(barCounterChannel)
                    .handle(message -> {
                        var command = (BrewRequestCommand) message.getPayload();
                        var brewOrderId = new OrderId(command.orderId().value());
                        orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewOrderId));
                    }).get();
        }

    }
}
