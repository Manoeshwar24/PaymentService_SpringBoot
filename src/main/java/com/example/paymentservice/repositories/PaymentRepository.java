package com.example.paymentservice.repositories;

import com.example.paymentservice.models.Payment;
import com.example.paymentservice.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Method to update the payment status with the given payment ID
    @Transactional
    default void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        findById(paymentId).ifPresent(payment -> {
            payment.setPaymentStatus(status);
            payment.setUpdatedAt(LocalDateTime.now());
        });
    }
}