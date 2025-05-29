package kr.co.won.tests.integration;

import kr.co.won.brew.EnableBrewModule;
import kr.co.won.brew.domain.OrderSheetId;
import kr.co.won.brew.domain.entity.OrderSheet;
import kr.co.won.brew.domain.entity.OrderSheetRepository;
import kr.co.won.brew.domain.entity.OrderSheetStatus;
import kr.co.won.order.EnableOrderModule;
import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.UserAccountId;
import kr.co.won.order.domain.entity.Order;
import kr.co.won.order.domain.entity.OrderRepository;
import kr.co.won.order.domain.entity.OrderStatus;
import kr.co.won.user.EnableUserModule;
import kr.co.won.user.domain.entity.UserAccount;
import kr.co.won.user.domain.entity.UserAccountRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.MessageRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableOrderModule
@EnableBrewModule
@EnableUserModule
public class CoffeehouseIntegrationTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeehouseIntegrationTestingApplication.class, args);
    }

    @Bean
    InitializingBean initData(OrderSheetRepository orderSheetRepository, OrderRepository orderRepository, UserAccountRepository userAccountRepository) {
        return () -> {
            var userAccountIdValue = "bb744f5a-2715-488b-aade-ebae5aa8f055";
            userAccountRepository.save(UserAccount.createCustomer(new kr.co.won.user.domain.UserAccountId(userAccountIdValue)));

            var newOrderIdValue = "7438b60b-7c68-4d55-a033-fa933e92832c";

            orderRepository.save(Order.create(new OrderId(newOrderIdValue), new UserAccountId(userAccountIdValue)));


            var acceptedOrderIdValue = "1a176aa8-e834-46e8-b293-0d0208ad1cd8";
            var confirmedOrderSheetIdValue = "e9c17eeb-2bbf-4087-acd3-9675eb6178db";
            orderRepository.save(new Order(new OrderId(acceptedOrderIdValue), new UserAccountId(userAccountIdValue), OrderStatus.ACCEPTED));
            orderSheetRepository.save(new OrderSheet(new OrderSheetId(confirmedOrderSheetIdValue), new kr.co.won.brew.domain.OrderId(acceptedOrderIdValue), OrderSheetStatus.CONFIRMED));
        };
    }

    /**
     * AMQP에 대한 사용하기 위한 RabbitMQ에대한 Queue 생성
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(new Queue("brew")); // routingKey에 대한 발행 값들을 담아주기 위한 Queue
        return rabbitAdmin;
    }

    /**
     * AMQP에서 사용할 Json 값을 변환하기 위한 Bean 등록
     *
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * AMQP를 이용한 메시지 수신을 위한 흐름에 대해서 정의하는 Bean 등록
     *
     * @param connectionFactory
     * @param brewRequestChannel
     * @return
     */
    @Bean
    public IntegrationFlow amqpInboundIntegrationFlow(ConnectionFactory connectionFactory, MessageChannel brewRequestChannel) {
        return IntegrationFlow.from(
                        Amqp.inboundAdapter(connectionFactory, "brew") // connectionFactory와 처리를 할 routingKey에 대한 값 지정
                )
                .handle(message -> {
                    brewRequestChannel.send(message); // inboundChannel에 메시지 전달한다.
                }) // 처리할 flow에 대한 정의
                .get();
    }

    @Bean
    public MessageChannel brewRequestChannel() {
        return new DirectChannel();
    }

    /**
     * AMQP를 이용한 메시지 전송을 위한 흐름에 대해서 정의하는 Bean 등록
     *
     * @param amqpTemplate
     * @param barCounterChannel
     * @return
     */
//    @Bean
//    public IntegrationFlow amqpOutboundIntegrationFlow(AmqpTemplate amqpTemplate, MessageChannel barCounterChannel) {
//        return IntegrationFlow.from(barCounterChannel)
//                .handle(
//                        Amqp.outboundAdapter(amqpTemplate) // 전달할 Adapter에 대한 설정
//                                .routingKey("brew") // 어디로 보낼줄지에 대한 값 설정
//                )
//                .get();
//    }

    /**
     * Annotation 방식을 이용해서 AMQP를 이용한 메시지 전송을 위한 흐름 정의
     *
     * @param amqpTemplate
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "barCounterChannel")
    public AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint amqpOutboundEndpoint = new AmqpOutboundEndpoint(amqpTemplate);
        amqpOutboundEndpoint.setRoutingKey("brew");
        return amqpOutboundEndpoint;
    }

    /**
     * MessageListener를 할 Container에 대한 정의하는 Bean 등록
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public MessageListenerContainer amqpListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.addQueueNames("brew"); // routingKey에 대한 Queue 생성
        return simpleMessageListenerContainer;
    }

    /**
     * Header에 있는 특정 값을 바탕으로 메시지 채널을 연결을 해주기 위한 Router에 대한 등록
     *
     * @return
     */
    @Bean
    @Router(inputChannel = "amqpInboundChannel")
    public MessageRouter messageRouter() {
        HeaderValueRouter router = new HeaderValueRouter("amqp_receivedRoutingKey");
        router.setChannelMapping("brew", "brewRequestChannel");
        return router;
    }

    @Bean
    public MessageChannel amqpInboundChannel() {
        ///  amqp에서 온 메시지는 순서대로 관리가 되어야 하기 때문에 Queue를 이용해서 관리하기 위한 QueueChannel을 생성한다.
        MessageChannel amqpInboundChannel = new QueueChannel();
        return amqpInboundChannel;
    }

    /**
     * 채널로 값이 전달이 되었을 때 해당 값을 전달할 채널에 대한 정의하는 Bean
     *
     * @param amqpListenerContainer
     * @param amqpInboundChannel
     * @return
     */
    @Bean
    public AmqpInboundChannelAdapter amqpInboundChannelAdapter(MessageListenerContainer amqpListenerContainer, MessageChannel amqpInboundChannel) {
        AmqpInboundChannelAdapter amqpInboundChannelAdapter = new AmqpInboundChannelAdapter(amqpListenerContainer);
        amqpInboundChannelAdapter.setOutputChannel(amqpInboundChannel); /// amqp로 온 데이터를 routing을 해서 받기 위한 QueueChannel 등록
        return amqpInboundChannelAdapter;
    }
//    /**
//     * 채널로 값이 전달이 되었을 때 해당 값을 전달할 채널에 대한 정의하는 Bean
//     *
//     * @param amqpListenerContainer
//     * @param brewRequestChannel
//     * @return
//     */
//    @Bean
//    public AmqpInboundChannelAdapter amqpInboundChannelAdapter(MessageListenerContainer amqpListenerContainer, MessageChannel brewRequestChannel) {
//        AmqpInboundChannelAdapter amqpInboundChannelAdapter = new AmqpInboundChannelAdapter(amqpListenerContainer);
//        amqpInboundChannelAdapter.setOutputChannel(brewRequestChannel); /// amqp로 온 데이터를 brewRequestChannel에 전달을 해주기 위한 설정
//        return amqpInboundChannelAdapter;
//    }


    /**
     * MessageChannel 등록
     */
    @Bean
    MessageChannel barCounterChannel() {
        /// 사용자가 생성한 채널
//        return new ObservableChannel();
        ///  Spring-integration의 기능을 이용한 채널
        return new DirectChannel();
    }


    @Bean
    RestTemplate defaultRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
