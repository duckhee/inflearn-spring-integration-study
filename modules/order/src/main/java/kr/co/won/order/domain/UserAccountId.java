package kr.co.won.order.domain;

public record UserAccountId(String value) {

    @Override
    public String toString() {
        return value;
    }
}
