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
import kr.co.won.user.domain.entity.UserAccount;
import kr.co.won.user.domain.entity.UserAccountRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author springrunner.kr@gmail.com
 */
@SpringBootApplication
@EnableOrderModule
@EnableBrewModule

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

    @Bean
    RestTemplate defaultRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
