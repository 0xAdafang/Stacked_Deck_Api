package com.adafangmarket.catalog.dto;

import org.hibernate.validator.constraints.UUID;

public record CategoryDto (
        UUID id, String name, String slug, UUID parentId, Integer position
) {}
