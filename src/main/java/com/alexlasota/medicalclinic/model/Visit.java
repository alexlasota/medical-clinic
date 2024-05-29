package com.alexlasota.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "email")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Visit other)) return false;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}