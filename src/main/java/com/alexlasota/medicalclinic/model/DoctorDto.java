package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DoctorDto {

    private Long id;
    private String phoneNumber;
    private String specialization;
    private List<Long> facilityIds;
    private UserDto userDto;

}
