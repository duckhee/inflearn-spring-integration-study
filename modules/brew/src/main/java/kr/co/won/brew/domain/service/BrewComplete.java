package kr.co.won.brew.domain.service;


import kr.co.won.brew.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface BrewComplete {

    void complete(OrderId orderId);
}
