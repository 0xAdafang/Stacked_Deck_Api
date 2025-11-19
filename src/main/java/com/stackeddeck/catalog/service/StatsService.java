package com.stackeddeck.catalog.service;


import com.stackeddeck.catalog.dto.StatsDto;
import com.stackeddeck.catalog.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StatsService {
    private final ProductRepository products;

    public StatsDto getStats() {
        long totalProducts = products.count();

        return StatsDto.builder()
                .totalCards(totalProducts)
                .totalCollectors(0L)
                .satisfactionRate(98.0)
                .build();
    }
}
