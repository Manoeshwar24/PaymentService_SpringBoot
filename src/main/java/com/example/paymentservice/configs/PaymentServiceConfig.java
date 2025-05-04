package com.example.paymentservice.configs;

import com.example.paymentservice.repositories.StripeMappingRepository;
import com.example.paymentservice.services.RazorPayInteractionService;
import com.example.paymentservice.services.StripeInteractionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentServiceConfig {

    @Bean
    public StripeInteractionService stripeInteractionService(StripeMappingRepository stripeMappingRepository) {
        return new StripeInteractionService(stripeMappingRepository);
    }

//    @Bean
//    public RazorPayInteractionService razorPayInteractionService() {
//        return new RazorPayInteractionService();
//    }
}
