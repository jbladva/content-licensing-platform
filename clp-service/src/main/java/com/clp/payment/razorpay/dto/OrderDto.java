package com.clp.payment.razorpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderId;
    private Integer amount;
    private Integer amount_paid;
    private Integer amount_due;
    private String currency;
    private String receipt;
    private String status;
}
