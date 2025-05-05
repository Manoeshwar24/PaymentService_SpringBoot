package com.example.paymentservice.services;

import com.example.paymentservice.models.StripeMapping;
import com.example.paymentservice.repositories.StripeMappingRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


public class StripeInteractionService implements PaymentGatewayInterface{

    private final StripeMappingRepository stripeMappingRepository;

    public StripeInteractionService(StripeMappingRepository stripeMappingRepository) {
        this.stripeMappingRepository = stripeMappingRepository;
    }
    // This service will handle all interactions with Stripe API

    // Method to create a Stripe payment link
    public PaymentLink createPaymentLink(Long orderId) throws StripeException {

        //Call orderService's and productService's API to fetch order details
        // For now, we will use a hardcoded product ID and quantity
        //Long productId = 1L;
        Long quantity = 2L;
        String currency = "INR";

        // Get StripeProduct and StripePrice from the StripeMapping table
        Optional<StripeMapping> stripeMapping = stripeMappingRepository.getStripeMappingByProductId(orderId);
        String stripePriceId = stripeMapping.get().getStripePriceId();

        //Interact with the Stripe API to create a payment link
        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .setCurrency(currency)
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(stripePriceId)
                                        .setQuantity(quantity)
                                        .build()
                        )
                        .setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl("https://youtube.com")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        PaymentLink paymentLink = PaymentLink.create(params);
        return paymentLink;
    }



    // Method to create a product in Stripe
    public Product createStripeProduct(String productName, String productDescription) throws StripeException {
        //Interact with StripeMapping table to get the

        // Interact with the Stripe API to create a product
        ProductCreateParams params =
                ProductCreateParams.builder().setName(productName).build();
        Product product = Product.create(params);

        return product;
    }

    // Method to create a price in Stripe
    public Price createStripePrice(String stripeProductId, String currency, Long amount) throws StripeException {
        PriceCreateParams params =
                PriceCreateParams.builder()
                        .setCurrency(currency)
                        .setUnitAmount(amount)
                        .build();

        Price price = Price.create(params);
        return price;
    }
}
