package com.swp391.jewelrysalesystem.models;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private String orderInfo;
    private String amount;
    private String returnUrl;
    private String ipnUrl;
}
