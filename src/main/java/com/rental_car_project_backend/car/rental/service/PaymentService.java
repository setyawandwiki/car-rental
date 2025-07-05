package com.rental_car_project_backend.car.rental.service;

import com.rental_car_project_backend.car.rental.dto.request.CreatePaymentRequest;
import com.rental_car_project_backend.car.rental.dto.request.PaymentNotificationRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreatedPaymentResponse;

public interface PaymentService {
    CreatedPaymentResponse createPayment(CreatePaymentRequest request);
    void handlePaymentNotification(PaymentNotificationRequest request);
}
