package com.example.paymentservice.services;
import com.example.paymentservice.strategies.PaymentGatewayStrategy;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentGatewayStrategy paymentGatewayStrategy;

    public PaymentService(PaymentGatewayStrategy paymentGatewayStrategy) {
        this.paymentGatewayStrategy = paymentGatewayStrategy;
    }

    // Method to create a Stripe payment link
    public PaymentLink createPaymentLink(Long orderId) throws StripeException {

        // Use PaymentStrategy to get an implementation of the Payment Gateway Interface
        // For now, we are getting the implementation on a random basis
        // In the future, we can use a more sophisticated strategy to select the payment gateway
        PaymentGatewayInterface paymentGateway = paymentGatewayStrategy.getPaymentGateway();

        // Call the payment gateway interface to create a payment link
        PaymentLink paymentLink = paymentGateway.createPaymentLink(orderId);
        return paymentLink;
    }
}
