package com.rental_car_project_backend.car.rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class XenditDisbursementCallback {
    private String event;
    private DisbursementData data;
    public static class DisbursementData{
        private String id;
        private String externalId;
        private String status;
        private Long amount;
    }
}
