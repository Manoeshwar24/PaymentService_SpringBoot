package com.example.paymentservice.controllers;

import com.example.paymentservice.services.StripeInteractionService;
import com.stripe.model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    private final StripeInteractionService stripeInteractionService;
    private static final String STRIPE_SIGNATURE_HEADER = "Stripe-Signature";

    public StripeWebhookController(StripeInteractionService stripeInteractionService) {
        this.stripeInteractionService = stripeInteractionService;
    }

    @PostMapping("/payment-webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader(STRIPE_SIGNATURE_HEADER) String signature) {

        try {
            Event event = stripeInteractionService.verifyAndConstructEvent(payload, signature);
            stripeInteractionService.handleStripeEvent(event);
            return ResponseEntity.ok("Webhook received successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid webhook payload: " + e.getMessage());
        }
    }
}