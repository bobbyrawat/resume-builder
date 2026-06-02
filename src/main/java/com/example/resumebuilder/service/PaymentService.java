package com.example.resumebuilder.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.resumebuilder.document.Payment;
import com.example.resumebuilder.document.User;
import com.example.resumebuilder.respository.PaymentRepository;
import com.example.resumebuilder.respository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    // CREATE ORDER
    public Payment createdOrder(String userId, String planType) {

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            RazorpayClient razorpayClient =
                    new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            int amount;

            if ("PREMIUM".equalsIgnoreCase(planType)) {
                amount = 19900;
            } else {
                throw new RuntimeException("Invalid plan type");
            }

            String receipt = "PREMIUM_" + UUID.randomUUID().toString().substring(0, 8);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);
            orderRequest.put("payment_capture", 1);

            Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            Payment payment = Payment.builder()
                    .userId(user.getId())
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .amount(amount)
                    .currency("INR")
                    .planType(planType)
                    .status("created")
                    .receipt(receipt)
                    .build();

            return paymentRepository.save(payment);

        } catch (Exception e) {
            log.error("Order creation failed", e);
            throw new RuntimeException("Payment order creation failed");
        }
    }

    // VERIFY PAYMENT (🔥 IMPORTANT FIX HERE)
    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) {

        try {

            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(options, razorpayKeySecret);

            if (!isValid) return false;

            Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setRazorpaySignature(razorpaySignature);
            payment.setStatus("paid");

            paymentRepository.save(payment);

            // 🔥 VERY IMPORTANT FIX
            User user = userRepository.findById(payment.getUserId())
                    .orElseThrow();

            user.setSubscriptionPlan("PREMIUM");
            userRepository.save(user);

            return true;

        } catch (Exception e) {
            log.error("Payment verification failed", e);
            return false;
        }
    }

    public List<Payment> getUserPayments(String userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Payment getPaymentDetails(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}