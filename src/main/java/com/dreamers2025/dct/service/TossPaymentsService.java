package com.dreamers2025.dct.service;

import lombok.NoArgsConstructor;
import org.springframework.http.*;
import com.dreamers2025.dct.domain.payment.dto.request.PaymentRequest;
import com.dreamers2025.dct.domain.payment.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TossPaymentsService {
    private final RestTemplate restTemplate;
    @Value("${tosspayments.test.secretkey}")
    private String SECRET_KEY;

    public TossPaymentsService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    public PaymentResponse requestPayment(PaymentRequest paymentRequest) {
        String url = "https://api.tosspayments.com/v1/payments";
        log.info("요청받은 객체"+paymentRequest.toString());
        // 인증 정보 (시크릿 키 Base64 인코딩)
        String encodedAuth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", paymentRequest.getAmount());
        requestBody.put("orderId", paymentRequest.getOrderId());
        requestBody.put("orderName", paymentRequest.getOrderName());
        requestBody.put("successUrl", paymentRequest.getSuccessUrl());
        requestBody.put("failUrl", paymentRequest.getFailUrl());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PaymentResponse> responseEntity = restTemplate.postForEntity(url, requestEntity, PaymentResponse.class);
        log.info("==================================================="+requestEntity.toString());

        return responseEntity.getBody();
    }

    // 결제 승인
    public PaymentResponse approvePayment(String paymentKey, String orderId, String amount) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        String encodedAuth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PaymentResponse> responseEntity = restTemplate.postForEntity(url, requestEntity, PaymentResponse.class);


        return responseEntity.getBody();
    }
}
