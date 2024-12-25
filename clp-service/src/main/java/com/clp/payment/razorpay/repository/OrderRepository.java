package com.clp.payment.razorpay.repository;

import com.clp.payment.razorpay.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByPublisherContent_Content_IdAndPublisherContent_Publisher_User_Id(Long contentId,
                                                                                          Long publisherUserId);
    Optional<Order> findByOrderIdAndPublisherContent_Content_Id(String orderId, Long publisherUserId);
}
