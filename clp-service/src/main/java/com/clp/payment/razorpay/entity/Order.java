package com.clp.payment.razorpay.entity;

import com.clp.entity.PublisherContent;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "razorpay_order")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String orderId;
    private Integer amount;
    private Integer amount_paid;
    private Integer amount_due;
    private String currency;
    private String receipt;
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    private PublisherContent publisherContent;
}
