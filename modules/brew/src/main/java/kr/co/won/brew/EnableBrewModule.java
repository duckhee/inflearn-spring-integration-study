package kr.co.won.brew;

import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.service.OrderSheetSubmission;
import kr.co.won.order.domain.message.BrewRequestCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;

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
//        @Bean
//        public IntegrationFlow requestBrewIntegrationFlow(OrderSheetSubmission orderSheetSubmission, MessageChannel barCounterChannel) {
//            return IntegrationFlow.from(barCounterChannel)
//                    .handle(message -> {
//                        var command = (BrewRequestCommand) message.getPayload();
//                        var brewOrderId = new OrderId(command.orderId().value());
//                        orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewOrderId));
//                    }).get();
//        }

        /**
         * Spring-Integration을 이용한 Message Channel에 대한 처리를 하는 Bean 정의
         * => AMQP를 이용한 메시지 처리
         *
         * @param orderSheetSubmission
         * @param brewRequestChannel
         * @return
         */
        @Bean
        public IntegrationFlow requestBrewIntegrationFlow(OrderSheetSubmission orderSheetSubmission, MessageChannel brewRequestChannel) {
            return IntegrationFlow.from(brewRequestChannel)
                    .handle(BrewRequestCommand.class, (payload, headers) -> {
                        var command = payload;
                        var brewOrderId = new OrderId(command.orderId().value());
                        orderSheetSubmission.submit(new OrderSheetSubmission.OrderSheetForm(brewOrderId));
                        return null; // 해당 값은 다음 목적지에 대한 값이 들어가야 한다.
                    }).get();
        }

        /**
         * BrewCompletedNotifyOrderChannel을 이름을 가지는 메시지 채널
         * -> 해당 기능은 BrewCompletedEvent를 받아서 연결을 해주기 위한 채널이다.
         *
         * @return
         */
        @Bean
        public MessageChannel brewCompletedNotifyOrderChannel() {
            return new DirectChannel();
        }

        /**
         * Order Service에 제조 완료를 알려주기 위한 흐름 설정
         *
         * @param environment
         * @param brewCompletedNotifyOrderChannel
         * @return
         */
        @Bean
        public IntegrationFlow notifyOrderIntegrationFlow(Environment environment, MessageChannel brewCompletedNotifyOrderChannel) {
            var orderUri = environment.getRequiredProperty("coffeehouse.brew.notify-brew-complete-uri", URI.class);
            return IntegrationFlow.from(brewCompletedNotifyOrderChannel)
                    .handle(Http.outboundChannelAdapter(orderUri).httpMethod(HttpMethod.POST)) ///  http의 RequestBody와 MessageChannel의 Payload 값이 같을 경우에은 바로 넣어줄 수 있다.

                    .get();
        }

        /**
         * User Service에 제조 완료를 알려주기 위한 채널 생성
         *
         * @return
         */
        @Bean
        public MessageChannel brewCompletedNotifyUserChannel() {

            return new DirectChannel();
        }

        /**
         * User Service에 제조 완료를 알려주기 위한 흐름 설정
         *
         * @param environment
         * @param brewCompletedNotifyUserChannel
         * @return
         */
        @Bean
        public IntegrationFlow notifyUserIntegrationFlow(Environment environment, MessageChannel brewCompletedNotifyUserChannel) {
            var userUri = environment.getRequiredProperty("coffeehouse.user.notify-brew-complete-uri", URI.class);
            return IntegrationFlow.from(brewCompletedNotifyUserChannel)
                    .handle(Http.outboundChannelAdapter(userUri).httpMethod(HttpMethod.POST))
                    .get();
        }
    }
}
