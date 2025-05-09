package com.example.paymentservice.controllers;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.services.StripeInteractionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    private final StripeInteractionService stripeInteractionService;

    private static final String STRIPE_SIGNATURE_HEADER = "Stripe-Signature";
    private static final String STRIPE_ENDPOINT_SECRET = "your_endpoint_secret";

    public StripeWebhookController(StripeInteractionService stripeInteractionService) {
        this.stripeInteractionService = stripeInteractionService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader(STRIPE_SIGNATURE_HEADER) String signature) {

        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, signature, STRIPE_ENDPOINT_SECRET);

            switch (event.getType()) {
                case "checkout.session.completed":
                    // Handle checkout session completion
                    Session session = (Session) event.getDataObjectDeserializer().getObject().get();
                    String paymentIntentId = session.getPaymentIntent();
                    if (paymentIntentId != null) {
                        // Retrieve the payment intent to get more details
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

            return ResponseEntity.ok("Webhook received successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid webhook payload: " + e.getMessage());
        }
    }

    private void handlePaymentIntent(PaymentIntent paymentIntent) throws StripeException {
        // Get the payment ID from metadata or client_reference_id
        String paymentId = paymentIntent.getMetadata().get("payment_id");

        // Update the payment status from the received event type
        PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentIntent.getStatus().toUpperCase());
        // Update payment status in your system
        if (paymentId != null) {
            stripeInteractionService.updatePaymentStatus(Long.valueOf(paymentId), paymentStatus);
        }
    }
}