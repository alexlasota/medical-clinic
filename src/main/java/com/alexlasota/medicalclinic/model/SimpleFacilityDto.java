package com.alexlasota.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleFacilityDto {

    private Long id;
    private String name;
    private String city;
    private String street;
}
