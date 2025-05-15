package com.example.paymentservice.services;

import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.models.StripeMapping;
import com.example.paymentservice.repositories.PaymentRepository;
import com.example.paymentservice.repositories.StripeMappingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class StripeInteractionService {
    
    private static final String STRIPE_ENDPOINT_SECRET = "your_endpoint_secret";

    private final StripeMappingRepository stripeMappingRepository;
    private final PaymentRepository paymentRepository;

    public StripeInteractionService(StripeMappingRepository stripeMappingRepository, PaymentRepository paymentRepository) {
        this.stripeMappingRepository = stripeMappingRepository;
        this.paymentRepository = paymentRepository;
    }
    // This service will handle all interactions with Stripe API

    // Method to create a Stripe payment link
    public PaymentLink createPaymentLink(Long orderId) throws StripeException {

        //Call orderService's and productService's API to fetch order details
        // For now, we will use a hardcoded product ID and quantity
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

    //Method to receive data from the webhook enabled in Stripe
    public void updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus) throws StripeException {

        //update the database with the payment status that we received from Stripe
        paymentRepository.updatePaymentStatus(paymentId, paymentStatus);
    }

    public Event verifyAndConstructEvent(String payload, String signature) throws StripeException {
        return Webhook.constructEvent(payload, signature, STRIPE_ENDPOINT_SECRET);
    }

    public void handleStripeEvent(Event event) throws StripeException {
        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer().getObject().get();
                String paymentIntentId = session.getPaymentIntent();
                if (paymentIntentId != null) {
                    PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
                    handlePaymentIntent(paymentIntent);
                }
                break;

            case "payment_intent.succeeded":
            case "payment_intent.payment_failed":
            case "payment_intent.canceled":
            case "payment_intent.created":
            case "payment_intent.processing":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                handlePaymentIntent(paymentIntent);
                break;
        }
    }

    private void handlePaymentIntent(PaymentIntent paymentIntent) throws StripeException {
        String paymentId = paymentIntent.getMetadata().get("payment_id");
        PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentIntent.getStatus().toUpperCase());
        
        if (paymentId != null) {
            updatePaymentStatus(Long.valueOf(paymentId), paymentStatus);
        }
    }
}