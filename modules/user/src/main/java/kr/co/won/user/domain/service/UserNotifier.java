package kr.co.won.user.domain.service;

import kr.co.won.user.domain.UserAccountId;
import kr.co.won.user.domain.entity.UserAccount;

@FunctionalInterface
public interface UserNotifier {

    void notify(UserAccount userAccount);
}
