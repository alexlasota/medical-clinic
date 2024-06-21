package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByMedicalUser_email(String email);

    Optional<Patient> deleteByMedicalUser_email(String email);
}