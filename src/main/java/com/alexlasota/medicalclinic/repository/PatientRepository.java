package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepository {

    private final List<Patient> patients;

    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatientByEmail(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public void editPatient(Patient patient, Patient newPatientData) {
        patient.setBirthday(newPatientData.getBirthday());
        patient.setEmail(newPatientData.getEmail());
        patient.setPassword(newPatientData.getPassword());
        patient.setLastName(newPatientData.getLastName());
        patient.setFirstName(newPatientData.getFirstName());
        patient.setIdCardNo(newPatientData.getIdCardNo());
        patient.setPhoneNumber(newPatientData.getPhoneNumber());
    }

    public void updatePatientPassword(Patient patient, String newPassword) {
        patient.setPassword(newPassword);
    }
}

