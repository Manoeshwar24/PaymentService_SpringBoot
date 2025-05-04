package com.example.paymentservice.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;


public class RazorPayInteractionService implements PaymentGatewayInterface {
    @Override
    public PaymentLink createPaymentLink(Long orderId) throws StripeException {
        return null;
    }

    @Override
    public Product createStripeProduct(String productName, String productDescription) throws StripeException {
        return null;
    }

    @Override
    public Price createStripePrice(String stripeProductId, String currency, Long amount) throws StripeException {
        return null;
    }
}
