package com.clp.payment.razorpay.controller;

import com.clp.payment.razorpay.dto.OrderDto;
import com.clp.payment.razorpay.dto.Razorpay;
import com.clp.payment.razorpay.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Order API")
@CrossOrigin(maxAge = 3600)
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("v1/payment")
    public ResponseEntity<Razorpay> createOrder(@RequestParam Double amount, @RequestParam Long contentId){
        return ResponseEntity.ok(orderService.createOrder(amount, contentId));
    }

    @GetMapping("v1/payment")
    public ResponseEntity<List<OrderDto>> getOrder(@RequestParam Long contentId){
        return ResponseEntity.ok(orderService.getOrders(contentId));
    }

    @GetMapping("v1/payment/order")
    public ResponseEntity<OrderDto> getOrderById(@RequestParam String orderId, @RequestParam Long contentId){
        return ResponseEntity.ok(orderService.getOrder(orderId, contentId));
    }

    @PutMapping("v1/payment/order/status")
    public ResponseEntity<String> paymentStatus(@RequestParam String orderId,
                                                @RequestParam Long contentId,
                                                @RequestParam String paymentStatus){
        return ResponseEntity.ok(orderService.setPaymentStatus(orderId, contentId,paymentStatus));
    }
}
