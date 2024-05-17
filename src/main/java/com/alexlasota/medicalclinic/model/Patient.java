package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

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

}