package kr.co.won.brew.domain.service;


import kr.co.won.brew.domain.OrderId;

/**
 * @author springrunner.kr@gmail.com
 */
@FunctionalInterface
public interface OrderSheetSubmission {

    /**
     * Submits an order sheet.
     *
     * @param orderSheetForm The form of the order to submit
     */
    void submit(OrderSheetForm orderSheetForm);

    record OrderSheetForm(OrderId orderId) {
    }
}
