package com.alexlasota.medicalclinic.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class VisitRequestDto {
    private Long doctorId;
    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
}