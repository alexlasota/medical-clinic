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
@EqualsAndHashCode
@ToString
@Entity
public class Doctor {

    @Id
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

    @OneToMany
    private List<Visit> visits = new ArrayList<>();
}

//Model doktora oprócz maila i
// hasła powinien zawierać imię, nazwisko, specjalizację oraz listę przypisanych placówek.
//Model placówki powinien zawierać nazwę (powinna być unikalna),
// miasto, kod pocztowy, ulice oraz numer budynku.
