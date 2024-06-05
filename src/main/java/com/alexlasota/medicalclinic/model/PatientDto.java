package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PatientDto {

    private Long id;
    private String idCardNo;
    private String phoneNumber;
    private String birthday;
    private UserDto userDto;

}
