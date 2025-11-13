package com.stackeddeck.checkout.repo;


import com.stackeddeck.checkout.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByStripePaymentIntentId(String stripePaymentIntentId);

}
