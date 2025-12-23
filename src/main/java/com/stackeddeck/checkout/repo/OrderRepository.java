package com.stackeddeck.checkout.repo;


import com.stackeddeck.checkout.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserIdOrderByCreatedAtDesc (UUID userId);
    List <Order> findAllByOrderByCreatedAtDesc();
    Optional<Order> findByStripePaymentIntentId(String stripePaymentIntentId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'PAID' OR o.status = 'SHIPPED'")
    Long sumTotalRevenue();

    long countByStatus(com.stackeddeck.checkout.enums.OrderStatus status);

}
