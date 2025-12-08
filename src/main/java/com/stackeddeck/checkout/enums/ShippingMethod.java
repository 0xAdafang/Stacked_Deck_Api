package com.stackeddeck.checkout.enums;

import lombok.Getter;

@Getter
public enum ShippingMethod {
    STANDARD("Standard Shipping (5-7 days)", 499),
    EXPRESS("Express Shipping (1-2 days)", 1499),
    FREE("Free Shipping (Over $100)", 0);

    private final String label;
    private final long price;

    ShippingMethod(String label, long price) {
        this.label = label;
        this.price = price;
    }

    public static ShippingMethod resolve(String type, long cartTotal) {

        if ("EXPRESS".equalsIgnoreCase(type)) {
            return EXPRESS;
        }


        if (cartTotal >= 10000) {
            return FREE;
        }


        return STANDARD;
    }
}
