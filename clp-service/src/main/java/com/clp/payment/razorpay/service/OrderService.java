package com.clp.payment.razorpay.service;

import com.clp.payment.razorpay.dto.OrderDto;
import com.clp.payment.razorpay.dto.Razorpay;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {

    Razorpay createOrder(Double amount, Long contentId);
    List<OrderDto> getOrders(Long contentId);
    OrderDto getOrder(String orderId, Long contentId);
    String setPaymentStatus(String orderId, Long contentId, String paymentStatus);
}
