package kr.co.won.user.domain.entity;


import kr.co.won.user.domain.UserAccountId;

public class UserAccount {

    private final UserAccountId userAccountId;

    public UserAccountId getUserAccountId() {
        return userAccountId;
    }

    public UserAccount(UserAccountId userAccountId) {
        this.userAccountId = userAccountId;
    }

    public static UserAccount createCustomer(UserAccountId userAccountId) {
        return new UserAccount(userAccountId);
    }
}
