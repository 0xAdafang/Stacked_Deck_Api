package com.stackeddeck.admin.service;

import com.stackeddeck.admin.dto.DashboardStatsDto;
import com.stackeddeck.catalog.repo.ProductRepository;
import com.stackeddeck.checkout.enums.OrderStatus;
import com.stackeddeck.checkout.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public DashboardStatsDto getStats() {
        Long revenue = orderRepository.sumTotalRevenue();
        if (revenue == null) revenue = 0L;

        long totalOrders = orderRepository.count();

        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);

        long totalProducts = productRepository.count();

        long lowStock = 0;

        return new DashboardStatsDto(
                revenue,
                totalOrders,
                pendingOrders,
                totalProducts,
                lowStock
        );
    }
}
