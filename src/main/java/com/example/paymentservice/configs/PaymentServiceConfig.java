package com.example.paymentservice.configs;

import com.example.paymentservice.repositories.PaymentRepository;
import com.example.paymentservice.repositories.StripeMappingRepository;
import com.example.paymentservice.services.PaymentGatewayInterface;
import com.example.paymentservice.services.RazorPayInteractionService;
import com.example.paymentservice.services.StripeInteractionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentServiceConfig {

    @Bean
    public StripeInteractionService stripeInteractionService(StripeMappingRepository stripeMappingRepository,
                                                             PaymentRepository paymentRepository) {
        return new StripeInteractionService(stripeMappingRepository, paymentRepository);
    }

//    @Bean
//    public RazorPayInteractionService razorPayInteractionService() {
//        return new RazorPayInteractionService();
//    }
}
