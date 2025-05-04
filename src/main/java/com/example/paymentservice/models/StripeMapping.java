package com.example.paymentservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StripeMapping extends BaseModel {

    private Long productId;
    private String stripeProductId;
    private String stripePriceId;
    private String productName;
    private String productDescription;
}
