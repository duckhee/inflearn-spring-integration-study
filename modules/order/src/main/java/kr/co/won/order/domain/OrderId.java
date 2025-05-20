package kr.co.won.order.domain;

public record OrderId(String value) {

    @Override
    public String toString() {
        return value;
    }
}
