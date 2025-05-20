package kr.co.won.user.domain;

public record UserAccountId(String value) {

    @Override
    public String toString() {
        return value;
    }
}
