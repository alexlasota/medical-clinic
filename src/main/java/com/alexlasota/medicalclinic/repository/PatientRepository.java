package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {
}
