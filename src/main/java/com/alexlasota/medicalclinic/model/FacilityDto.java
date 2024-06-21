package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class FacilityDto {

    private Long id;
    private String name;
    private String city;
    private String postNumber;
    private String street;
    private String buildingNumber;
    private List<Long> doctorIds;
}
