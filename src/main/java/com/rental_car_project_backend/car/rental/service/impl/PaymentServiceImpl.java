package com.rental_car_project_backend.car.rental.service.impl;

import com.rental_car_project_backend.car.rental.dto.request.XenditDisbursementCallback;
import com.rental_car_project_backend.car.rental.dto.request.payment.CreatePaymentRequest;
import com.rental_car_project_backend.car.rental.dto.request.payment.PaymentNotificationRequest;
import com.rental_car_project_backend.car.rental.dto.response.payment.CreatedPaymentResponse;
import com.rental_car_project_backend.car.rental.entity.*;
import com.rental_car_project_backend.car.rental.enums.OrderStatus;
import com.rental_car_project_backend.car.rental.exceptions.*;
import com.rental_car_project_backend.car.rental.repository.*;
import com.rental_car_project_backend.car.rental.service.PaymentService;
import com.xendit.exception.XenditException;
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
                .orElseThrow(() -> new PaymentExceptions("payment not found"));

        orders.setStatus(OrderStatus.PAID);
        orders.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(orders);

        CompanyCar companyCar = companyCarRepository
                .findByIdCompany(orders.getIdCompanyCars()).orElseThrow(() ->
                        new CompanyCarNotFoundException("Company Car not found"));

        Double totalAmount = payments.getAmount();
        Double adminFee = totalAmount * 0.1;
        Double vendorAmount = totalAmount - adminFee;
        Companies companies = companyRepository.findById(companyCar.getIdCompany())
                .orElseThrow(()-> new CompanyNotFoundException("company not found"));
        Users users = userRepository.findById(companies.getIdUser()).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        Vendor vendor = vendorRepository
                .findByCompanyId(companyCar.getIdCompany()).orElseThrow(()
                        -> new PaymentExceptions("Vendor not found "));

        vendor.setPendingWithDrawl(vendor.getPendingWithDrawl() + vendorAmount);
        vendor.setUpdatedAt(LocalDateTime.now());
        vendorRepository.save(vendor);

        PlatFormProfit platFormProfit = PlatFormProfit.builder()
                .profitAmount(adminFee)
                .companyId(companyCar.getIdCompany())
                .percentage(10.0)
                .createdAt(LocalDateTime.now())
                .build();
        platFormProfitRepository.save(platFormProfit);

        Map<String, Object> params = new HashMap<>();
        params.put("external_id", "DISB-"+orders.getId());
        params.put("amount", vendorAmount);
        params.put("bank_code", users.getBankCode());
        params.put("account_holder_name", users.getFullName());
        params.put("account_number", users.getAccountNumber());
        params.put("description", "Pembayaran untuk order #" + orders.getId());

        try {
            Disbursement disbursement = Disbursement.create(params);
        } catch (XenditException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleDisbursementCompleted(XenditDisbursementCallback callback) {
        if(!"disbursement.completed".equals(callback.getEvent())) {
            throw new PaymentExceptions("Ignored");
        };

        Integer orderId = Integer.parseInt(callback.getData().getExternalId().replace("DISB-", ""));
        Double vendorAmount = callback.getData().getAmount().doubleValue();

        Orders orders = orderRepository.findById(orderId).orElseThrow(() ->
                new OrdersNotFoundException("Order not found with order id " + orderId));
        CompanyCar companyCar = companyCarRepository.findById(orders.getIdCompanyCars()).orElseThrow(()
                -> new CompanyCarNotFoundException("Company car not found"));
        Vendor vendors = vendorRepository.findByCompanyId(companyCar.getIdCompany()).orElseThrow(() ->
                new RuntimeException("Vendor not found"));
        vendors.setAvailableBalance(vendors.getAvailableBalance() + vendorAmount);
        vendors.setPendingWithDrawl(vendors.getPendingWithDrawl() - vendorAmount);
        vendors.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendors);
    }
}
