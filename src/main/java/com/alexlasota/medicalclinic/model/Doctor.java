package com.alexlasota.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//Konstruktor ze wszystkimi polami nie jest konieczny, ponieważ zwykle obiekty encji są tworzone
// przez Hibernate podczas ich odczytu z bazy danych lub przez inne części aplikacji (np. serwisy).
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String specialization;

    @ManyToMany
    @JoinTable(
            name = "doctor_facility",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<Visit> visits = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Doctor other)) return false;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}