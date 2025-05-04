package com.example.paymentservice.repositories;

import com.example.paymentservice.models.StripeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeMappingRepository extends JpaRepository<StripeMapping, Long> {

    //get StripeProductID for give productID
    Optional<StripeMapping> getStripeMappingByProductId(Long productId);
}
