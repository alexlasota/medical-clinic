package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Entity
public class Visit {

    @Id
    private Long id;
    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor", referencedColumnName = "docId")
    private Doctor doctor;
    private boolean available;
}
