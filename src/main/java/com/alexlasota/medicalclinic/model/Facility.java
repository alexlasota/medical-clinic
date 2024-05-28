package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Facility {

    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    private String city;
    private String postNumber;
    private String street;
    private String buildingNumber;

    @ManyToMany(mappedBy = "facilities")
    private List<Doctor> doctors;

}
