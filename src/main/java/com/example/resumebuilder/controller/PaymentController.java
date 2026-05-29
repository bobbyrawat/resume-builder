package com.example.resumebuilder.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resumebuilder.document.Payment;
import com.example.resumebuilder.service.PaymentService;
import static com.example.resumebuilder.util.AppConstants.PREMIUM;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createdOrder(
            @RequestBody Map<String, String> request,
            Authentication authentication
    ) {

        try {

            String planType = request.get("planType");

            if (!PREMIUM.equalsIgnoreCase(planType)) {
                return ResponseEntity.badRequest().body(
                        Map.of("message", "Invalid plan type")
                );
            }

            Payment payment = paymentService.createdOrder(
                    authentication.getName(),
                    planType
            );

            return ResponseEntity.ok(Map.of(
                    "orderId", payment.getRazorpayOrderId(),
                    "amount", payment.getAmount(),
                    "currency", payment.getCurrency(),
                    "receipt", payment.getReceipt()
            ));

        } catch (Exception e) {

            log.error("Create order failed", e);

            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "message", "Something went wrong",
                            "errors", e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request) {

        boolean isValid = paymentService.verifyPayment(
                request.get("razorpay_order_id"),
                request.get("razorpay_payment_id"),
                request.get("razorpay_signature")
        );

        if (isValid) {
            return ResponseEntity.ok(Map.of(
                    "message", "Payment verified successfully",
                    "status", "success"
            ));
        }

        return ResponseEntity.badRequest().body(
                Map.of(
                        "message", "Payment verification failed",
                        "status", "failed"
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(Authentication authentication) {

        List<Payment> payments =
                paymentService.getUserPayments(authentication.getName());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId) {

        Payment paymentDetails =
                paymentService.getPaymentDetails(orderId);

        return ResponseEntity.ok(paymentDetails);
    }
}