package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Entity
public class Patient {

    @Id
    private String email;
    private String idCardNo;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String birthday;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits = new ArrayList<>();
}