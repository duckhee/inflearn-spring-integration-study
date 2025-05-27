package kr.co.won.tests.integration;

import kr.co.won.brew.domain.entity.OrderSheetRepository;
import kr.co.won.brew.domain.entity.OrderSheetStatus;
import kr.co.won.order.domain.OrderId;
import kr.co.won.order.domain.entity.OrderRepository;
import kr.co.won.order.domain.entity.OrderStatus;
import kr.co.won.order.domain.service.OrderAcceptance;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OrderFulfillmentTests {

    @Autowired
    OrderAcceptance orderAcceptance;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderSheetRepository orderSheetRepository;

    @Test
    void acceptOrder() {
        // given
        var orderId = new OrderId("7438b60b-7c68-4d55-a033-fa933e92832c");

        // when
        orderAcceptance.acceptOrder(orderId);

        // then
        var order = orderRepository.findById(orderId).orElse(null);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);

        /// 비동기 테스트를 위한 {@link org.awaitility.Awaitility}를 이용한 3초 이내 응답
        Awaitility.await()
                .atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
                    var orderSheet = orderSheetRepository.findByOrderId(new kr.co.won.brew.domain.OrderId("7438b60b-7c68-4d55-a033-fa933e92832c")).orElse(null);
                    assertThat(orderSheet.getOrderSheetStatus()).isEqualTo(OrderSheetStatus.SUBMITTED);
                });


    }
}
