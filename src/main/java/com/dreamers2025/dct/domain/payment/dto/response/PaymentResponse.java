package com.dreamers2025.dct.domain.payment.dto.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentKey;
    private String orderId;
    private String amount;
}
