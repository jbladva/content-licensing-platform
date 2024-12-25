package com.clp.payment.razorpay.service;

import com.clp.entity.Content;
import com.clp.entity.Writer;
import com.clp.payment.razorpay.entity.Order;
import org.springframework.stereotype.Component;

@Component
public interface RazorpayService {

    Order createOrder(Double amount, Writer writer, Content content);
}

