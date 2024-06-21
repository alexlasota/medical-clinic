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
    private SimplePatientDto patient;
    private SimpleDoctorDto doctor;
    private boolean available;
}
