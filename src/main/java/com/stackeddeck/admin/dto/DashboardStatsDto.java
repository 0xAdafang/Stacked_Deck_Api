package com.stackeddeck.admin.dto;

public record DashboardStatsDto (
        Long totalRevenue,
        long totalOrders,
        long pendingOrders,
        long totalProducts,
        long lowStockProducts
) {}
