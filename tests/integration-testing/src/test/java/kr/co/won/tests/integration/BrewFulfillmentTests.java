package kr.co.won.tests.integration;

import kr.co.won.brew.domain.OrderId;
import kr.co.won.brew.domain.entity.OrderSheetRepository;
import kr.co.won.brew.domain.entity.OrderSheetStatus;
import kr.co.won.brew.domain.service.BrewComplete;
import kr.co.won.order.domain.entity.OrderRepository;
import kr.co.won.order.domain.entity.OrderStatus;
import kr.co.won.user.domain.service.UserNotifier;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BrewFulfillmentTests {

    @Autowired
    BrewComplete brewComplete;

    @Autowired
    OrderSheetRepository orderSheetRepository;

    @Autowired
    OrderRepository orderRepository;

    @MockBean
    UserNotifier userNotifier;

    @Test
    void completeBrew() {
        // given
        var orderId = new OrderId("1a176aa8-e834-46e8-b293-0d0208ad1cd8");

        // when
        brewComplete.complete(orderId);

        // then
        var orderSheet = orderSheetRepository.findByOrderId(orderId).orElse(null);
        assertThat(orderSheet.getOrderSheetStatus()).isEqualTo(OrderSheetStatus.PROCESSED);

        var order = orderRepository.findById(new kr.co.won.order.domain.OrderId("1a176aa8-e834-46e8-b293-0d0208ad1cd8")).orElse(null);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);

        verify(userNotifier, Mockito.times(1)).notify(any());
    }
}
