package com.clp.payment.razorpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Razorpay {

    private String applicationFee;
    private String razorpayOrderId;
    private String secretKey;
    private String paymentId;
    private String notes;
    private String imageURL;
    private String theme;
    private String merchantName;
    private String purchaseDescription;
    private String customerName;
    private String customerEmail;
    private String customerContact;
}
