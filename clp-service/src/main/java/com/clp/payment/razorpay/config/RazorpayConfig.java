package com.clp.payment.razorpay.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Value("${razorpay.api.key.id}")
    private String SECRET_ID;

    @Value("${razorpay.api.key.secret}")
    private String SECRET_KEY;

    @Bean
    public RazorpayClient getRazorpayClient() throws RazorpayException {
        return new RazorpayClient(SECRET_ID, SECRET_KEY);
    }
}
