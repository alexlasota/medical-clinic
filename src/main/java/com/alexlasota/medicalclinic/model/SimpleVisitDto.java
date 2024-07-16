package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleVisitDto {

    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
    private SimpleDoctorDto doctor;

}