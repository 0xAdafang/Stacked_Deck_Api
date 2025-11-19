package com.stackeddeck.catalog;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(indexes = {
        @Index(name="idx_cat_slug", columnList="slug", unique=true),
        @Index(name="idx_cat_parent", columnList="parent_id")
})
public class Category {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String slug;

    private String image;
    private String icon;
    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime releaseDate;
    private String series;
    private boolean isFeatured;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;
    private Integer position = 0;
    private boolean active = true;
}