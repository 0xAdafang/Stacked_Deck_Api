package com.stackeddeck.catalog.dto;

import lombok.Builder;

@Builder
public record StatsDto (
    long totalCards,
    long totalCollectors,
    double satisfactionRate
) {}
