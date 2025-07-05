package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.payment.CreatePaymentRequest;
import com.rental_car_project_backend.car.rental.dto.request.payment.PaymentNotificationRequest;
import com.rental_car_project_backend.car.rental.dto.response.payment.CreatedPaymentResponse;
import com.rental_car_project_backend.car.rental.entity.*;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import com.rental_car_project_backend.car.rental.exceptions.CompanyCarNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.CompanyNotFoundException;
import com.rental_car_project_backend.car.rental.exceptions.PaymentExceptions;
import com.rental_car_project_backend.car.rental.exceptions.UserNotFoundException;
import com.rental_car_project_backend.car.rental.repository.*;
import com.rental_car_project_backend.car.rental.service.PaymentService;
import com.xendit.model.Disbursement;
import com.xendit.model.Invoice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final VendorRepository vendorRepository;
    private final CompanyCarRepository companyCarRepository;
    private final PlatFormProfitRepository platFormProfitRepository;
    private final CompanyRepository companyRepository;
    @Override
    public CreatedPaymentResponse createPayment(CreatePaymentRequest request) {
        Orders orders = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new PaymentExceptions("order with id " + request.getOrderId() + " is not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("there is no user with email " + email));
        String externalId = "ORDER-" + orders.getId();
        Map<String, Object> params = new HashMap<>();
        params.put("external_id", externalId);
        params.put("payer_email", user.getEmail());
        params.put("currency", "IDR");
        params.put("invoice_duration", 86400);
        params.put("amount", orders.getPriceTotal());

        Invoice invoice;
        try{
            invoice = Invoice.create(params);
        }catch (Exception e){
            throw new PaymentExceptions("Failed created invoice to xendit " + e.getMessage());
        }

        Payments payments = Payments.builder()
                .createdAt(LocalDateTime.now())
                .orderId(orders.getId())
                .externalId(externalId)
                .amount(orders.getPriceTotal())
                .orderStatus(OrderStatus.PENDING)
                .invoiceId(invoice.getId())
                .build();

        paymentRepository.save(payments);
        return CreatedPaymentResponse.builder()
                .id(payments.getId())
                .orderId(payments.getOrderId())
                .invoiceId(payments.getInvoiceId())
                .amount(payments.getAmount())
                .createdAt(payments.getCreatedAt())
                .externalId(payments.getExternalId())
                .orderStatus(payments.getOrderStatus())
                .paidAt(payments.getPaidAt())
                .build();
    }

    @Override
    public void handlePaymentNotification(PaymentNotificationRequest request) {
        if (OrderStatus.PAID != (request.getStatus())) return;
        Payments payments = paymentRepository
                .findByExternalId(request.getId())
                .orElseThrow(() ->
                        new PaymentExceptions("Payment not found with id " + request.getPaymentId()));

        if(OrderStatus.PAID == payments.getOrderStatus()){
            return;
        }

        payments.setOrderStatus(OrderStatus.PAID);
        payments.setPaidAt(request.getPaidAt());
        paymentRepository.save(payments);

        Orders orders = orderRepository.findById(payments.getOrderId())
                .orElseThrow(() -> new PaymentExceptions("paymenot not found"));

        orders.setStatus(OrderStatus.PAID);
        orders.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(orders);

        CompanyCar companyCar = companyCarRepository
                .findByIdCompany(orders.getIdCompanyCars()).orElseThrow(() ->
                        new CompanyCarNotFoundException("Company Car not found"));
        Vendor vendor = vendorRepository
                .findByCompanyId(companyCar.getIdCompany()).orElseThrow(()
                        -> new PaymentExceptions("Vendor not found "));

        Double totalAmount = payments.getAmount();
        Double adminFee = totalAmount * 0.1;
        Double vendorAmount = totalAmount - adminFee;
        Companies companies = companyRepository.findById(companyCar.getIdCompany())
                .orElseThrow(()-> new CompanyNotFoundException("company not found"));
        Users users = userRepository.findById(companies.getIdUser()).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        PlatFormProfit platFormProfit = PlatFormProfit.builder()
                .profitAmount(adminFee)
                .companyId(companyCar.getIdCompany())
                .percentage(5.0)
                .createdAt(LocalDateTime.now())
                .build();


        Disbursement disbursement = Disbursement.create()
        platFormProfitRepository.save(platFormProfit);

    }
}
