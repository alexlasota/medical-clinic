package com.alexlasota.medicalclinic.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitRequestDto {
    private Long doctorId;
    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
}