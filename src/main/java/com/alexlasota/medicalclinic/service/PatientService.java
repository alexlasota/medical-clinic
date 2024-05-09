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
                .orElseThrow(() -> new RuntimeException("Patient with given email doesnt exist"));
    }

    public void addPatient(Patient patient) {
        patientRepository.addPatient(patient);
    }

    public void removePatientByEmail(String email) {
        patientRepository.removePatientByEmail(email);
    }

    public Patient editPatient(String email, Patient newPatientData) {
        Patient toEditPatient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient with given email doesnt exist"));

        patientRepository.editPatient(toEditPatient, newPatientData);
        return toEditPatient;
    }

    public Patient updatePassword(String email, Patient newPassword) {
        Patient patient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient with given email doesn't exist"));

        if (isValidPassword(newPassword.getPassword())) {
            patientRepository.updatePatientPassword(patient, newPassword.getPassword());
        } else {
            throw new RuntimeException("Invalid password format");
        }
        return patient;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.matches(".*\\d.*");
    }
    // \\d - dowolna cyfra 0-9. * ciąg znaków
}



