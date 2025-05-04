package com.example.paymentservice.strategies;

import com.example.paymentservice.services.PaymentGatewayInterface;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;

@Component
public class PaymentGatewayStrategy {

    private final List<PaymentGatewayInterface> paymentGatewayInterfaceList;

    public PaymentGatewayStrategy(List<PaymentGatewayInterface> paymentGatewayInterfaceList){
        this.paymentGatewayInterfaceList = paymentGatewayInterfaceList;
    }

    public PaymentGatewayInterface getPaymentGateway(){

        Random random = new Random();
        return paymentGatewayInterfaceList.get(random.nextInt(paymentGatewayInterfaceList.size()));
    }
}
