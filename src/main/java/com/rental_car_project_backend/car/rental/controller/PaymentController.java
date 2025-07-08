package com.rental_car_project_backend.car.rental.controller;

import com.rental_car_project_backend.car.rental.dto.request.payment.XenditDisbursementCallback;
import com.rental_car_project_backend.car.rental.dto.request.payment.CreatePaymentRequest;
import com.rental_car_project_backend.car.rental.dto.request.payment.PaymentNotificationRequest;
import com.rental_car_project_backend.car.rental.dto.response.payment.CreatedPaymentResponse;
import com.rental_car_project_backend.car.rental.service.PaymentService;
import com.xendit.exception.XenditException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping
    public ResponseEntity<CreatedPaymentResponse> createPaymentResponse(@RequestBody CreatePaymentRequest request){
        CreatedPaymentResponse payment = paymentService.createPayment(request);
        return ResponseEntity.ok(payment);
    }
    @PostMapping("/webhook/payment")
    public ResponseEntity<String> handlePaymentNotification(@RequestBody PaymentNotificationRequest request)
            throws XenditException {
        paymentService.handlePaymentNotification(request);
        return ResponseEntity.ok("Payment Processed");
    }
    @PostMapping("/webhook/disbursement")
    public ResponseEntity<String> handleDisbursementNotification(@RequestBody XenditDisbursementCallback callback){
        paymentService.handleDisbursementCompleted(callback);
        return ResponseEntity.ok("Disbursement Processed");
    }
}
