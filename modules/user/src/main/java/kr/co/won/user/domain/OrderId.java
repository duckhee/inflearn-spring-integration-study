package kr.co.won.user.domain;

public record OrderId(String value) {

    @Override
    public String toString() {
        return value;
    }
}
