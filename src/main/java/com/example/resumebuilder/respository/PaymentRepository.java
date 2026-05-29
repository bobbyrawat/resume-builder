package com.example.resumebuilder.respository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.resumebuilder.document.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Optional <Payment> findByRazorpayOrderId(String razorPayOrderId);

    Optional <Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    List <Payment> findByUserIdOrderByCreatedAtDesc(String userId);

    List <Payment> findByStatus(String status);



}
