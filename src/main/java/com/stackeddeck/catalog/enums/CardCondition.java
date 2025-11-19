package com.stackeddeck.catalog.enums;

import lombok.Getter;

@Getter
public enum CardCondition {
    MINT("Mint", "M", "Perfect condition, straight from pack"),
    NEAR_MINT("Near Mint", "NM", "Minor flaws only visible under close inspection"),
    LIGHTLY_PLAYED("Lightly Played", "LP", "Minor wear visible, suitable for play"),
    MODERATELY_PLAYED("Moderately Played", "MP", "Noticeable wear, still playable"),
    HEAVILY_PLAYED("Heavily Played", "HP", "Significant wear, major flaws"),
    DAMAGED("Damaged", "DMG", "Major damage, creases, tears or water damage");

    private final String displayName;
    private final String abbreviation;
    private final String description;

    CardCondition(String displayName, String abbreviation, String description) {
        this.displayName = displayName;
        this.abbreviation = abbreviation;
        this.description = description;
    }
}