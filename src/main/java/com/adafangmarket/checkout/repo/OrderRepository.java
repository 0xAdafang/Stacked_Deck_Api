package com.adafangmarket.checkout.repo;


import com.adafangmarket.checkout.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {
}
