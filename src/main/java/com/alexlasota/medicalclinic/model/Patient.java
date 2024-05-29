package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Patient other)) return false;

        return Objects.equals(getEmail(), other.getEmail());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
