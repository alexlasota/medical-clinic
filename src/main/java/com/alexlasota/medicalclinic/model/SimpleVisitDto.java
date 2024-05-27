package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class SimpleVisitDto {

    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
    private SimpleDoctorDto doctor;
}
