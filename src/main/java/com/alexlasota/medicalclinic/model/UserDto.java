package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}