package com.stackeddeck.payment.service;

import com.stackeddeck.payment.Payment;
import com.stackeddeck.payment.dto.PaymentInitRequest;
import com.stackeddeck.payment.dto.PaymentInitResponse;
import com.stackeddeck.payment.enums.PaymentStatus;
import com.stackeddeck.payment.repo.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.frontend.base-url}")
    private String frontendUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Transactional
    public PaymentInitResponse createCheckoutSession(PaymentInitRequest request) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/payment/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(frontendUrl + "/checkout")
                    .setCustomerEmail(request.getEmail())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(request.getCurrency())
                                                    .setUnitAmount(request.getAmount())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Stacked Deck Order")
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();

            Session session = Session.create(params);

            Payment payment = Payment.builder()
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .userEmail(request.getEmail())
                    .stripeSessionId(session.getId())
                    .status(PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);

            return new PaymentInitResponse(session.getUrl());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe checkout session", e);
        }
    }

    @Transactional
    public void fulfillPayment(String stripeSessionId) {
        Optional<Payment> paymentOpt = paymentRepository.findByStripeSessionId(stripeSessionId);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();

            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                return;
            }

            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            System.out.println("✅ Payment confirmed for user: " + payment.getUserEmail());

        } else {
            System.out.println("⚠️ Payment with session ID " + stripeSessionId + " not found.");
        }

    }


}
