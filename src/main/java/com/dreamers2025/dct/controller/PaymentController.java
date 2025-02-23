package com.dreamers2025.dct.controller;

import com.dreamers2025.dct.domain.payment.dto.request.PaymentRequest;
import com.dreamers2025.dct.domain.payment.dto.response.PaymentResponse;
import com.dreamers2025.dct.service.TossPaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
@Slf4j
public class PaymentController {

    private final TossPaymentsService tossPaymentsService;

    public PaymentController(TossPaymentsService tossPaymentsService) {
        this.tossPaymentsService = tossPaymentsService;
    }

    @PostMapping("/request")
    public ResponseEntity<PaymentResponse> requestPayment(@RequestBody PaymentRequest requestDTO) {
        PaymentResponse response = tossPaymentsService.requestPayment(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<PaymentResponse> approvePayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam String amount) {
        PaymentResponse response = tossPaymentsService.approvePayment(paymentKey, orderId, amount);
        return ResponseEntity.ok(response);
    }
}
