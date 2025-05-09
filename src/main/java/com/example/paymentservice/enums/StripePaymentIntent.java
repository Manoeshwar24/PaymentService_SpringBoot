package com.example.paymentservice.enums;

public enum StripePaymentIntent {
    PAYMENT_INTENT_CREATED("payment_intent.created"),
    PAYMENT_INTENT_UPDATED("payment_intent.updated"),
    PAYMENT_INTENT_PAYMENT_FAILED("payment_intent.payment_failed"),
    PAYMENT_INTENT_PAYMENT_SUCCEEDED("payment_intent.payment_succeeded"),
    PAYMENT_INTENT_CANCELED("payment_intent.canceled");

    private final String eventType;

    StripePaymentIntent(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
