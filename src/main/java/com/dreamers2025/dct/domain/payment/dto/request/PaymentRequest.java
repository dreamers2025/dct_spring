package com.dreamers2025.dct.domain.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String amount;
    private String orderId;
    private String orderName;
    private String successUrl;
    private String failUrl;
}
