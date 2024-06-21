package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimplePatientDto {

    private Long id;
    private String firstName;
    private String lastName;
}
