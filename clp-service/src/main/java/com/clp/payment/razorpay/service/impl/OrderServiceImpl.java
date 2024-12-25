package com.clp.payment.razorpay.service.impl;

import com.clp.entity.PublisherContent;
import com.clp.entity.User;
import com.clp.entity.Writer;
import com.clp.payment.razorpay.dto.OrderDto;
import com.clp.payment.razorpay.dto.Razorpay;
import com.clp.payment.razorpay.entity.Order;
import com.clp.payment.razorpay.repository.OrderRepository;
import com.clp.payment.razorpay.service.OrderService;
import com.clp.payment.razorpay.service.RazorpayService;
import com.clp.repository.PublisherContentRepository;
import com.clp.security.UserContextHolder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final PublisherContentRepository publisherContentRepository;
    private final RazorpayService razorpayService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Value("${razorpay.api.key.id}")
    private String SECRET_ID;

    @Autowired
    public OrderServiceImpl(PublisherContentRepository publisherContentRepository,
                            RazorpayService razorpayService,
                            OrderRepository orderRepository,
                            ModelMapper modelMapper) {
        this.publisherContentRepository = publisherContentRepository;
        this.razorpayService = razorpayService;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Razorpay createOrder(Double amount, Long contentId) {
        User user = UserContextHolder.getUser();
        PublisherContent publisherContent = publisherContentRepository
                .findByContent_IdAndPublisher_User_Id(contentId, user.getId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Content not found with respective publisher")
                );

        Order order = razorpayService.createOrder(amount,
                publisherContent.getContent().getWriter(),
                publisherContent.getContent());
        order.setPublisherContent(publisherContent);

        Writer writer = publisherContent.getContent().getWriter();
        orderRepository.save(order);
        return getRazorPay(order, writer);
    }

    @Override
    public List<OrderDto> getOrders(Long contentId) {
        User user = UserContextHolder.getUser();
        List<Order> orders = orderRepository
                .findAllByPublisherContent_Content_IdAndPublisherContent_Publisher_User_Id(contentId, user.getId());

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        return modelMapper.map(orders, new TypeToken<List<OrderDto>>() {
        }.getType());
    }

    @Override
    public OrderDto getOrder(String orderId, Long contentId) {

        Order order = orderRepository.findByOrderIdAndPublisherContent_Content_Id(orderId, contentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
        );

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public String setPaymentStatus(String orderId, Long contentId, String paymentStatus) {

        Order order = orderRepository.findByOrderIdAndPublisherContent_Content_Id(orderId, contentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
        );

        if (paymentStatus.equalsIgnoreCase("paid")) {
            PublisherContent publisherContent = order.getPublisherContent();
            publisherContent.setAmount(publisherContent.getAmount() - order.getAmount());
            publisherContentRepository.save(publisherContent);
        }
        order.setStatus(paymentStatus);
        orderRepository.save(order);

        return "Status updated successfully";
    }

    private Razorpay getRazorPay(Order order, Writer writer) {
        return Razorpay.builder()
                .applicationFee(order.getAmount().toString())
                .customerName(writer.getName())
                .customerEmail(writer.getUser().getEmail())
                .merchantName("Test")
                .purchaseDescription("TEST PAYMENT")
                .razorpayOrderId(order.getOrderId())
                .secretKey(SECRET_ID)
                .imageURL("/logo")
                .theme("#F37254")
                .notes("notes" + order.getOrderId()).build();
    }
}
