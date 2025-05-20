package kr.co.won.user.domain.service.business;

import kr.co.won.user.domain.entity.UserAccount;
import kr.co.won.user.domain.service.UserNotifier;
import org.springframework.stereotype.Service;

@Service
public class UserNotifierProcessor implements UserNotifier {


    @Override
    public void notify(UserAccount userAccount) {
        // ----------------------------------------------------------
        // Notify user
        // ----------------------------------------------------------
    }
}
