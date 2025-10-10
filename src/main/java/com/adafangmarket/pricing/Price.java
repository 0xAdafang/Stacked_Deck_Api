package com.adafangmarket.pricing;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {
    @Column(nullable = false) private Long baseAmount; // cents
    @Column(nullable = false) private String currency; // "CAD"
    private Long promoAmount;
    private Instant promoStart;
    private Instant promoEnd;

    public Long getEffectiveAmount() {
        if (promoAmount != null && promoStart != null && promoEnd != null &&
        Instant.now().isAfter(promoStart) && Instant.now().isBefore(promoEnd)) {
            return promoAmount;
        }
        return baseAmount;
    }

}
