package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.RequestPaymentLinkDTO;
import com.example.paymentservice.dtos.ResponsePaymentLinkDTO;
import com.example.paymentservice.services.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    //post mapping for creating a payment link
    @PostMapping("/paymentLink/")
    public ResponseEntity<ResponsePaymentLinkDTO> createPaymentLink(@RequestBody RequestPaymentLinkDTO request){
        ResponsePaymentLinkDTO response = new ResponsePaymentLinkDTO();
        try {
            String paymentLink = paymentService.createPaymentLink(request.getOrderId()).getUrl();
            response.setPaymentLink(paymentLink);
        }
        catch(StripeException e){
            response.setErrorMessage("Error creating payment link: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            response.setErrorMessage("An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
