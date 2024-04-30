package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getPatients() {
        return patientRepository.getPatients();
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email doesnt exist"));
    }

    public void addPatient(Patient patient) {
        patientRepository.addPatient(patient);
    }

    public void removePatientByEmail(String email) {
        patientRepository.removePatientByEmail(email);
    }

    public Patient editPatient(String email, Patient newPatientData) {
        Patient toEditPatient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient with given email doesnt exist"));

        patientRepository.editPatient(toEditPatient, newPatientData);
        return toEditPatient;
    }
    public Optional<Patient> updatePassword(String email, Patient newPassword) {
        Optional<Patient> optionalPatient = Optional.ofNullable(getPatientByEmail(email));
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            if (isValidPassword(newPassword.getPassword())) {
                patientRepository.updatePatientPassword(patient, newPassword.getPassword());
                return Optional.of(patient);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }


    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.matches(".*\\d.*");
    }
        // \\d - dowolna cyfra 0-9. * ciąg znaków
    }



