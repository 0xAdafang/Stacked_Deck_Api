package com.stackeddeck.checkout.service;

import com.stackeddeck.catalog.Inventory;
import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.repo.InventoryRepository;
import com.stackeddeck.catalog.repo.ProductRepository;
import com.stackeddeck.checkout.Cart;
import com.stackeddeck.checkout.CartItem;
import com.stackeddeck.checkout.Order;
import com.stackeddeck.checkout.OrderItem;
import com.stackeddeck.checkout.dto.CheckoutRequest;
import com.stackeddeck.checkout.enums.OrderStatus;
import com.stackeddeck.checkout.repo.CartRepository;
import com.stackeddeck.checkout.repo.OrderRepository;
import com.stackeddeck.notifications.service.NotificationService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final NotificationService notificationService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Transactional
    public Order createOrder(CheckoutRequest request) {
        UUID userId = request.userId();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        long total = 0L;
        for(CartItem item : cart.getItems()) {
            Product product = productRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
            Inventory inv = inventoryRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found"));
            if (inv.tryReserve(item.getQuantity())) {
                // reserved successfully -> accumulate total
                total += item.getPriceAtAdd() * item.getQuantity();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for " + item.getSku());
            }
        }

        String paymentIntentId;
        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(total)
                    .setCurrency("cad")
                    .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods
                            .builder()
                            .setEnabled(true)
                            .build())
                    .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            paymentIntentId = paymentIntent.getId();
        } catch (StripeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment intent creation failed");
        }

        final Order order = Order.builder()
                .userId(userId)
                .totalAmount(total)
                .stripePaymentIntentId(paymentIntentId)
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .sku(item.getSku())
                        .quantity(item.getQuantity())
                        .price(item.getPriceAtAdd())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartRepository.delete(cart);

        notificationService.sendNotification(userId.toString(), "Your order #" + order.getId() + " has been created !", order.getId().toString(), null );

        return order;

    }

    @Transactional
    public void handlePaymentWebhook(String paymentIntentId, String status) {
        Order order = orderRepository.
                findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderRepository.save(order);
    }
}
