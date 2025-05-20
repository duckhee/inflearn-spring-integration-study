package kr.co.won.user.domain.service;

import kr.co.won.user.domain.OrderId;
import kr.co.won.user.domain.UserAccountId;

public interface UserIdResolver {

    default UserAccountId resolveUserAccountIdByOrderId(OrderId orderId) {
        return null;
    }

}
