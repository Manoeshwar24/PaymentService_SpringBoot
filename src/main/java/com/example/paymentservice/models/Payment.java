package com.example.paymentservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Payment extends BaseModel {
    private String paymentLink;
    private Long orderId;
    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.ORDINAL)
    private PaymentGateway paymentGateway;

    // Constructor, getters, and setters
    public Payment() {
        this.paymentStatus = PaymentStatus.PENDING;
        this.setCreatedAt(java.time.LocalDateTime.now());
        this.setUpdatedAt(java.time.LocalDateTime.now());
    }
}
