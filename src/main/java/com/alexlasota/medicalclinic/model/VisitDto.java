package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class VisitDto {

    private Long id;
    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
    private Patient patient;
    private Doctor doctor;
    private boolean available;
}
