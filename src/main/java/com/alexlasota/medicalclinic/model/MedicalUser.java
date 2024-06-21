package com.alexlasota.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class MedicalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MedicalUser medicalUser)) return false;

        return id != null && id.equals(medicalUser.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}