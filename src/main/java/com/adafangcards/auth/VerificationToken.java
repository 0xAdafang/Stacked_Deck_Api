package com.adafangcards.auth;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

public class VerificationToken {

    @Id @GeneratedValue private UUID id;
    @Column(nullable=false, unique=true) private String token;
    @Column(nullable=false) private UUID userId;
    @Column(nullable=false) private Instant expiryAt;
    private Instant consumedAt;
}
