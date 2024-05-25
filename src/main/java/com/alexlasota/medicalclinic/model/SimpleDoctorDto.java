package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SimpleDoctorDto {

        private Long id;
        private String firstName;
        private String phoneNumber;
        private String specialization;

    }

