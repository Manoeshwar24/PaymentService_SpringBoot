package com.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePaymentLinkDTO {
    public String paymentLink;
    public String errorMessage;
}
