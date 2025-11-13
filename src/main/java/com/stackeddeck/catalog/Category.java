package com.stackeddeck.catalog;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(indexes = {
        @Index(name="idx_cat_slug", columnList="slug", unique=true),
        @Index(name="idx_cat_parent", columnList="parent_id")
})
public class Category {
    @Id @GeneratedValue private UUID id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String slug;
    @ManyToOne(fetch = FetchType.LAZY) private Category parent;
    private Integer position = 0;
}