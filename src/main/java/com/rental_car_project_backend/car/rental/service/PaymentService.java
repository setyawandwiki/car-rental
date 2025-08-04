package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.payment.XenditDisbursementCallback;
import com.rental_car_project_backend.car.rental.dto.request.payment.CreatePaymentRequest;
import com.rental_car_project_backend.car.rental.dto.request.payment.PaymentNotificationRequest;
import com.rental_car_project_backend.car.rental.dto.response.payment.CreatedPaymentResponse;
import com.xendit.exception.XenditException;

public interface PaymentService {
    CreatedPaymentResponse createPayment(CreatePaymentRequest request);
    void handlePaymentNotification(PaymentNotificationRequest request) throws XenditException;
    void handleDisbursementCompleted(XenditDisbursementCallback callback);
    void processPendingPayments();
}
