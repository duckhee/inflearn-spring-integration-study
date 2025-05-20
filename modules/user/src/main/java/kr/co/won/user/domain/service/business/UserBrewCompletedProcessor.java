package kr.co.won.user.domain.service.business;

import kr.co.won.user.domain.OrderId;
import kr.co.won.user.domain.entity.UserAccountNotFoundException;
import kr.co.won.user.domain.entity.UserAccountRepository;
import kr.co.won.user.domain.service.UserBrewCompleted;
import kr.co.won.user.domain.service.UserIdResolver;
import kr.co.won.user.domain.service.UserNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserBrewCompletedProcessor implements UserBrewCompleted {

    private final UserAccountRepository userAccountRepository;
    private final UserIdResolver userIdResolver;
    private final UserNotifier userNotifier;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserBrewCompletedProcessor(UserAccountRepository userAccountRepository, UserIdResolver userIdResolver, UserNotifier userNotifier) {
        this.userAccountRepository = Objects.requireNonNull(userAccountRepository, "UserAccountRepository must not be null");
        this.userIdResolver = Objects.requireNonNull(userIdResolver, "UserIdResolver must not be null");
        this.userNotifier = Objects.requireNonNull(userNotifier, "UserNotifier must not be null");
    }

    @Override
    public void notify(OrderId orderId) {
        logger.info("Notify user-brew-complete: %s".formatted(orderId));
        var userAccountId = userIdResolver.resolveUserAccountIdByOrderId(orderId);
        var userAccount = userAccountRepository.findById(userAccountId).orElseThrow(UserAccountNotFoundException::new);
        userNotifier.notify(userAccount);
    }
}
