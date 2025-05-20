package kr.co.won.user.domain.entity;

import kr.co.won.user.domain.UserAccountId;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserAccountRepository {

    default Optional<UserAccount> findById(UserAccountId userAccountId) {
        return Optional.empty();
    }

    default UserAccount save(UserAccount userAccount) {
        return userAccount;
    }
}
