package com.adafangmarket.catalog.dto;

import java.util.UUID;

public record CategoryDto (
        UUID id, String name, String slug, UUID parentId, Integer position
) {}
