package com.clp.payment.razorpay.service.impl;

import com.clp.entity.Content;
import com.clp.entity.Writer;
import com.clp.payment.razorpay.dto.OrderDto;
import com.clp.payment.razorpay.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RazorpayServiceImpl implements RazorpayService {

    private final RazorpayClient client;

    @Autowired
    public RazorpayServiceImpl(RazorpayClient client) {
        this.client = client;
    }

    public com.clp.payment.razorpay.entity.Order createOrder(Double amount, Writer writer, Content content) {
        try {
            return mapOrderDto(createRazorPayOrder(amount, writer, content));

        } catch (Exception e) {
            log.error("createOrder error", e);
        }
        return null;
    }

    private Order createRazorPayOrder(Double amount, Writer writer, Content content) throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("payment_capture", 1);
        options.put("receipt", "receipt#" + writer.getId() + content.getId());
        return client.orders.create(options);
    }

    private com.clp.payment.razorpay.entity.Order mapOrderDto(Order order) {
        return com.clp.payment.razorpay.entity.Order.builder()
                .orderId(order.get("id"))
                .amount_paid(order.get("amount_paid"))
                .amount_due(order.get("amount_due"))
                .amount(order.get("amount"))
                .status(order.get("status"))
                .receipt(order.get("receipt"))
                .currency(order.get("currency"))
                .build();
    }
}
