package com.stackeddeck.catalog.dto;

import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        String slug,
        String image,
        UUID parentId,
        Integer position,
        Long cardCount
) {}
