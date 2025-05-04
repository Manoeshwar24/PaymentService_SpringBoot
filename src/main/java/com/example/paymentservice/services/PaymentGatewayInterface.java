package com.example.paymentservice.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import org.springframework.stereotype.Service;

@Service
public interface PaymentGatewayInterface {

    PaymentLink createPaymentLink(Long orderId) throws StripeException;
    Product createStripeProduct(String productName, String productDescription) throws StripeException;
    Price createStripePrice(String stripeProductId, String currency, Long amount) throws StripeException;
}
