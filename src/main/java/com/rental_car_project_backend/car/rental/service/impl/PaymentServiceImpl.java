package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.CreatePaymentRequest;
import com.rental_car_project_backend.car.rental.dto.request.PaymentNotificationRequest;
import com.rental_car_project_backend.car.rental.dto.response.CreatedPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    @Override
    public CreatedPaymentResponse createPayment(CreatePaymentRequest request) {
        return null;
    }

    @Override
    public void handlePaymentNotification(PaymentNotificationRequest request) {

    }
}
