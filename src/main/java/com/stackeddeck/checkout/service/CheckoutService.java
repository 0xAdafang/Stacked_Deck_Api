package com.stackeddeck.checkout.service;

import com.stackeddeck.checkout.Cart;
import com.stackeddeck.checkout.dto.CartDto;
import com.stackeddeck.checkout.dto.CheckoutSummaryDto;
import com.stackeddeck.checkout.enums.ShippingMethod;
import com.stackeddeck.checkout.repo.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartService;
    private final CartRepository carts;

    public CheckoutSummaryDto getCheckoutSummary(UUID userId, String shippingType, String country) {

        CartDto cart = cartService.getMyCart(userId);

        long subtotal = cart.totalAmount();

        ShippingMethod method = ShippingMethod.resolve(shippingType, subtotal);
        long shippingCost = method.getPrice();

        long taxableAmount = subtotal + shippingCost;
        long tax = (long) (taxableAmount * 0.15);

        long grandTotal = taxableAmount + tax;

        return new CheckoutSummaryDto(
                subtotal,
                cart.discountAmount(),
                shippingCost,
                tax,
                grandTotal,
                method.getLabel()
        );

    }
}
