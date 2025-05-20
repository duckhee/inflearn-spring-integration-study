package kr.co.won.user.data.http;

import kr.co.won.user.domain.OrderId;
import kr.co.won.user.domain.UserAccountId;
import kr.co.won.user.domain.service.UserIdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Component
public class UserIdResolverHttpClient implements UserIdResolver {

    private final RestTemplate restTemplate;
    private final URI findOrderInfoUri;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserIdResolverHttpClient(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "RestTemplate must not be null");
        this.findOrderInfoUri = environment.getProperty("coffeehouse.search-order.uri", URI.class);
        logger.info("findOrderInfoUri: {}", findOrderInfoUri.toString());
    }

    @Override
    public UserAccountId resolveUserAccountIdByOrderId(OrderId orderId) {
        logger.info("Find order using web API, orderID : {}", orderId);

        var order = restTemplate.getForObject(findOrderInfoUri + "/" + orderId.value(), Order.class);
        return order.ordererId();
    }

    public record Order(OrderId id, UserAccountId ordererId) {

    }
}
