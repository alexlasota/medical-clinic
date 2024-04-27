package com.alexlasota.medicalclinic;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientManager {

    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public Patient getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatientByEmail(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public boolean updatePatientByEmail(String email, Patient editedPatient) {
        Patient patient = getPatientByEmail(email);
        if (patient != null) {
            patient.updateDetails(editedPatient);
            return true;
        }
        return false;
    }
}


